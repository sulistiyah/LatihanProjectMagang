package com.magang.projectmaganglatihan.tflite


import com.magang.projectmaganglatihan.tflite.SimilarityClassifier.Recognition
import android.graphics.Bitmap
import android.graphics.RectF
import kotlin.Throws
import android.content.res.AssetManager
import android.os.Trace
import android.util.Pair
import org.tensorflow.lite.Interpreter
import com.magang.projectmaganglatihan.env.Logger
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.sqrt

/**
 * Wrapper for frozen detection models trained using the Tensorflow Object Detection API:
 * - https://github.com/tensorflow/models/tree/master/research/object_detection
 * where you can find the training code.
 *
 * To use pretrained models in the API or convert to TF Lite models, please see docs for details:
 * - https://github.com/tensorflow/models/blob/master/research/object_detection/g3doc/detection_model_zoo.md
 * - https://github.com/tensorflow/models/blob/master/research/object_detection/g3doc/running_on_mobile_tensorflowlite.md#running-our-model-on-android
 */
class TFLiteObjectDetectionAPIModel private constructor() : SimilarityClassifier {
    private var isModelQuantized = false

    // Config values.
    private var inputSize = 0

    // Pre-allocated buffers.
    private val labels = Vector<String>()
    private lateinit var intValues: IntArray

    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private lateinit var outputLocations: Array<Array<FloatArray>>

    // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the classes of detected boxes
    private lateinit var outputClasses: Array<FloatArray>

    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private lateinit var outputScores: Array<FloatArray>

    // numDetections: array of shape [Batchsize]
    // contains the number of detected boxes
    private lateinit var numDetections: FloatArray
    private lateinit var embeedings: Array<FloatArray>
    private lateinit var imgData: ByteBuffer
    private var tfLite: Interpreter? = null

    // Face Mask Detector Output
    private val output: Array<FloatArray> = arrayOf()
    private val registered = HashMap<String?, Recognition?>()
    override fun register(recognition: Recognition?) {
        registered[recognition.toString()] = recognition
    }

    // looks for the nearest embeeding in the dataset (using L2 norm)
    // and returns the pair <id, distance>
    private fun findNearest(emb: FloatArray): Pair<String?, Float>? {
        var ret: Pair<String?, Float>? = null
        for ((name, value) in registered) {
            val knownEmb = (value!!.extra as Array<FloatArray>?)!![0]
            var distance = 0f
            for (i in emb.indices) {
                val diff = emb[i] - knownEmb[i]
                distance += diff * diff
            }
            distance = sqrt(distance.toDouble()).toFloat()
            if (ret == null || distance < ret.second) {
                ret = Pair(name, distance)
            }
        }
        return ret
    }

    override fun recognizeImage(bitmap: Bitmap?, storeExtra: Boolean): List<Recognition?> {
        // Log this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage")
        Trace.beginSection("preprocessBitmap")
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        bitmap!!.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        imgData.rewind()
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues[i * inputSize + j]
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((pixelValue shr 16 and 0xFF).toByte())
                    imgData.put((pixelValue shr 8 and 0xFF).toByte())
                    imgData.put((pixelValue and 0xFF).toByte())
                } else { // Float model
                    imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }
        Trace.endSection() // preprocessBitmap

        // Copy the input data into TensorFlow.
        Trace.beginSection("feed")
        val inputArray = arrayOf<Any?>(imgData)
        Trace.endSection()

// Here outputMap is changed to fit the Face Mask detector
        val outputMap: MutableMap<Int, Any> = HashMap()
        embeedings = Array(1) { FloatArray(OUTPUT_SIZE) }
        outputMap[0] = embeedings


        // Run the inference call.
        Trace.beginSection("run")
        //tfLite.runForMultipleInputsOutputs(inputArray, outputMapBack);
        tfLite!!.runForMultipleInputsOutputs(arrayOf(inputArray), outputMap)
        Trace.endSection()

//    String res = "[";
//    for (int i = 0; i < embeedings[0].length; i++) {
//      res += embeedings[0][i];
//      if (i < embeedings[0].length - 1) res += ", ";
//    }
//    res += "]";
        var distance = Float.MAX_VALUE
        val id = "0"
        var label: String? = "?"
        if (registered.size > 0) {
            //LOGGER.i("dataset SIZE: " + registered.size());
            val nearest = findNearest(embeedings[0])
            if (nearest != null) {
                val name = nearest.first
                label = name
                distance = nearest.second
                LOGGER.i("nearest: $name - distance: $distance")
            }
        }
        val numDetectionsOutput = 1
        val recognitions = ArrayList<Recognition?>(numDetectionsOutput)
        val rec = Recognition(
            id,
            label,
            distance,
            RectF()
        )
        recognitions.add(rec)
        if (storeExtra) {
            rec.extra = embeedings
        }
        Trace.endSection()
        return recognitions
    }

    override fun enableStatLogging(logStats: Boolean?) {}
    override val statString: String
        get() = ""

    override fun close() {}
//    override fun setNumThreads(num_threads: Int) {
//        if (tfLite != null) tfLite!!.setNumThreads(num_threads)
//    }
//
//    override fun setUseNNAPI(isChecked: Boolean) {
//        if (tfLite != null) tfLite!!.setUseNNAPI(isChecked)
//    }

    companion object {
        private val LOGGER = Logger()

        //private static final int OUTPUT_SIZE = 512;
        private const val OUTPUT_SIZE = 192

        // Only return this many results.
        private const val NUM_DETECTIONS = 1

        // Float model
        private const val IMAGE_MEAN = 128.0f
        private const val IMAGE_STD = 128.0f

        // Number of threads in the java app
        private const val NUM_THREADS = 4

        /** Memory-map the model file in Assets.  */
        @Throws(IOException::class)
        private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
            val fileDescriptor = assets.openFd(modelFilename)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

        /**
         * Initializes a native TensorFlow session for classifying images.
         *
         * @param assetManager The asset manager to be used to load assets.
         * @param modelFilename The filepath of the model GraphDef protocol buffer.
         * @param labelFilename The filepath of label file for classes.
         * @param inputSize The size of image input
         * @param isQuantized Boolean representing model is quantized or not
         */
        @Throws(IOException::class)
        fun create(
            assetManager: AssetManager,
            modelFilename: String,
            labelFilename: String,
            inputSize: Int,
            isQuantized: Boolean
        ): SimilarityClassifier {
            val d = TFLiteObjectDetectionAPIModel()
            val actualFilename = labelFilename.split("file:///android_asset/").toTypedArray()[1]
            val labelsInput = assetManager.open(actualFilename)
            val br = BufferedReader(InputStreamReader(labelsInput))
            var line: String
            while (br.readLine().also { line = it } != null) {
                LOGGER.w(line)
                d.labels.add(line)
            }
            br.close()
            d.inputSize = inputSize
            try {
                d.tfLite = Interpreter(loadModelFile(assetManager, modelFilename))
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            d.isModelQuantized = isQuantized
            // Pre-allocate buffers.
            val numBytesPerChannel: Int = if (isQuantized) {
                1 // Quantized
            } else {
                4 // Floating point
            }
            d.imgData =
                ByteBuffer.allocateDirect(d.inputSize * d.inputSize * 3 * numBytesPerChannel)
            d.imgData.order(ByteOrder.nativeOrder())
            d.intValues = IntArray(d.inputSize * d.inputSize)
//            d.tfLite!!.setNumThreads(NUM_THREADS)
            d.outputLocations = Array(1) { Array(NUM_DETECTIONS) { FloatArray(4) } }
            d.outputClasses = Array(1) { FloatArray(NUM_DETECTIONS) }
            d.outputScores = Array(1) { FloatArray(NUM_DETECTIONS) }
            d.numDetections = FloatArray(1)
            return d
        }
    }
}