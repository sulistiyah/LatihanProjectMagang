package com.magang.projectmaganglatihan.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.*
import android.hardware.camera2.CameraCharacteristics
import android.media.ImageReader.OnImageAvailableListener
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.util.Size
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.api.RetrofitClient
import com.magang.projectmaganglatihan.customview.OverlayView
import com.magang.projectmaganglatihan.env.BorderedText
import com.magang.projectmaganglatihan.env.ImageUtils
import com.magang.projectmaganglatihan.env.Logger
import com.magang.projectmaganglatihan.model.SetDataWajahResponse
import com.magang.projectmaganglatihan.storage.SharedPrefManager
import com.magang.projectmaganglatihan.tflite.SimilarityClassifier
import com.magang.projectmaganglatihan.tflite.SimilarityClassifier.Recognition
import com.magang.projectmaganglatihan.tflite.TFLiteObjectDetectionAPIModel
import com.magang.projectmaganglatihan.tracking.MultiBoxTracker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class DetectorActivity : CameraActivity(), OnImageAvailableListener {

    private lateinit var sharedPref: SharedPrefManager
    private var trackingOverlay: OverlayView? = null
    private var sensorOrientation: Int? = null
    private var detector: SimilarityClassifier? = null
    private var lastProcessingTimeMs: Long = 0
    private var rgbFrameBitmap: Bitmap? = null
    private var croppedBitmap: Bitmap? = null
    private var cropCopyBitmap: Bitmap? = null
    private var computingDetection = false
    private var addPending = false

    //private boolean adding = false;
    private var timestamp: Long = 0
    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null

    //private Matrix cropToPortraitTransform;
    private var tracker: MultiBoxTracker? = null
    private var borderedText: BorderedText? = null

    // Face detector
    private var faceDetector: FaceDetector? = null

    // here the preview image is drawn in portrait way
    private var portraitBmp: Bitmap? = null

    // here the face is cropped and drawn
    private var faceBmp: Bitmap? = null
    private var addPicture : FloatingActionButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPrefManager(this)

        addPicture = findViewById(R.id.fabCamera)
        addPicture!!.setOnClickListener{
            onAddClick()
        }

        // Real-time contour detection of multiple faces
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()
        val detector = FaceDetection.getClient(options)
        faceDetector = detector

    }

    private fun onAddClick() {
        addPending = true
        //Toast.makeText(this, "click", Toast.LENGTH_LONG ).show();
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onPreviewSizeChosen(size: Size?, rotation: Int?) {

        val textSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, resources.displayMetrics)
        borderedText = BorderedText(textSizePx)
        borderedText?.setTypeface(Typeface.MONOSPACE)
        tracker = MultiBoxTracker(this)
        try {
            detector = TFLiteObjectDetectionAPIModel.create(
                assets,
                TF_OD_API_MODEL_FILE,
                TF_OD_API_LABELS_FILE,
                TF_OD_API_INPUT_SIZE,
                TF_OD_API_IS_QUANTIZED)
            //cropSize = TF_OD_API_INPUT_SIZE;
        } catch (e: IOException) {
            e.printStackTrace()
            LOGGER.e(e, "Exception initializing classifier!")
            Toast.makeText(applicationContext, "Classifier could not be initialized", Toast.LENGTH_SHORT).show()
            finish()
        }
        previewWidth = size?.width!!
        previewHeight = size.height
        sensorOrientation = rotation!! - getScreenOrientation()
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation)
        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight)
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
        val targetW: Int
        val targetH: Int
        if (sensorOrientation == 90 || sensorOrientation == 270) {
            targetH = previewWidth
            targetW = previewHeight
        } else {
            targetW = previewWidth
            targetH = previewHeight
        }
        val cropW = (targetW / 2.0).toInt()
        val cropH = (targetH / 2.0).toInt()
        croppedBitmap = Bitmap.createBitmap(cropW, cropH, Bitmap.Config.ARGB_8888)
        portraitBmp = Bitmap.createBitmap(targetW, targetH, Bitmap.Config.ARGB_8888)
        faceBmp = Bitmap.createBitmap(TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE, Bitmap.Config.ARGB_8888)
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            cropW, cropH,
            sensorOrientation!!, MAINTAIN_ASPECT)

//        frameToCropTransform =
//            ImageUtils.getTransformationMatrix(
//                    previewWidth, previewHeight,
//                    previewWidth, previewHeight,
//                sensorOrientation!!, MAINTAIN_ASPECT);

        cropToFrameTransform = Matrix()
        frameToCropTransform?.invert(cropToFrameTransform)

        val frameToPortraitTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            targetW, targetH,
            sensorOrientation!!, MAINTAIN_ASPECT)


        trackingOverlay = findViewById(R.id.tracking_overlay)
        trackingOverlay!!.addCallback { canvas ->
            tracker!!.draw(canvas!!)
            if (isDebug) {
                tracker!!.drawDebug(canvas)
            }
        }

        tracker!!.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation!!)
    }

    override fun processImage() {
        ++timestamp
        val currTimestamp = timestamp
        trackingOverlay!!.postInvalidate()

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage()
            return
        }
        computingDetection = true
        LOGGER.i("Preparing image $currTimestamp for detection in bg thread.")
        rgbFrameBitmap!!.setPixels(
            getRgbBytes(),
            0,
            previewWidth,
            0,
            0,
            previewWidth,
            previewHeight)
        readyForNextImage()
        val canvas = Canvas(croppedBitmap!!)
        canvas.drawBitmap(rgbFrameBitmap!!, frameToCropTransform!!, null)
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap!!)
        }
        val image : InputImage = InputImage.fromBitmap(croppedBitmap!!, 0)
        faceDetector
            ?.process(image)
            ?.addOnSuccessListener(OnSuccessListener { faces ->
                fun onSuccess(faces: List<Face?>) {
                    if (faces.size == 0) {
                        updateResults(currTimestamp, LinkedList())
                        return
                    }
                    runInBackground {
                        onFacesDetected(currTimestamp, faces as List<Face>, addPending)
                        addPending = false
                    }
                }
            })
    }


    override fun getLayoutId(): Int {
        return R.layout.camera_connection_fragment
    }


    override fun getDesiredPreviewFrameSize(): Size {
        return DESIRED_PREVIEW_SIZE
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum class DetectorMode {
        TF_OD_API
    }

//    override fun setUseNNAPI(isChecked: Boolean) {
//        runInBackground { detector!!.setUseNNAPI(isChecked) }
//    }

    override fun onClick(v: View?) {

    }

//    override fun setNumThreads(numThreads: Int) {
//        runInBackground { detector!!.setNumThreads(numThreads) }
//    }

    // Face Processing
    private fun createTransform(
        srcWidth: Int,
        srcHeight: Int,
        dstWidth: Int,
        dstHeight: Int,
        applyRotation: Int,
    ): Matrix {
        val matrix = Matrix()
        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {
                LOGGER.w("Rotation of %d % 90 != 0", applyRotation)
            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f)

            // Rotate around origin.
            matrix.postRotate(applyRotation.toFloat())
        }

//        // Account for the already applied rotation, if any, and then determine how
//        // much scaling is needed for each axis.
//        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;
//        final int inWidth = transpose ? srcHeight : srcWidth;
//        final int inHeight = transpose ? srcWidth : srcHeight;
        if (applyRotation != 0) {

            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f)
        }
        return matrix
    }

    private fun showAddFaceDialog(rec: Recognition) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.facedetector_image_dialog, null)
        val ivFace = dialogLayout.findViewById<ImageView>(R.id.dlg_image)
        val tvTitle = dialogLayout.findViewById<TextView>(R.id.dlg_title)

        tvTitle.text = "Pastikan ini wajah anda"
        ivFace.setImageBitmap(rec.crop)
        builder.setPositiveButton("OK") { dlg, _ ->
            detector!!.register(rec)
//            setDataWajah()
            dlg.dismiss()
        }
        builder.setNegativeButton("CANCEL") { dlg, _ ->
            dlg.dismiss()
        }
        builder.setView(dialogLayout)
        builder.show()
    }

    private fun updateResults(currTimestamp: Long, mappedRecognitions: List<Recognition>) {
        tracker!!.trackResults(mappedRecognitions, currTimestamp)
        trackingOverlay!!.postInvalidate()
        computingDetection = false
        //adding = false;
//        if (mappedRecognitions.size > 0) {
        if (mappedRecognitions.size > 0) {
            LOGGER.i("Adding results")
            val rec : Recognition = mappedRecognitions[0]
            if (rec.extra != null) {
                showAddFaceDialog(rec)
                setDataWajah()
            }
        }

        runOnUiThread {
            showFrameInfo(previewWidth.toString() + "x" + previewHeight)
            showCropInfo(croppedBitmap!!.width.toString() + "x" + croppedBitmap!!.height)
            showInference(lastProcessingTimeMs.toString() + "ms")
        }
    }

    private fun setDataWajah() {

        val employeeId: RequestBody = sharedPref.employeeId.toRequestBody("text/plain".toMediaTypeOrNull())

        RetrofitClient.instance.postSetDataWajah("Bearer ${sharedPref.tokenLogin}", employeeId)
            .enqueue(object : Callback<SetDataWajahResponse> {
                override fun onResponse(
                    call: Call<SetDataWajahResponse>,
                    response: Response<SetDataWajahResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {

                            SharedPrefManager.getInstance(this@DetectorActivity).saveCheckData(true)


                        } else {
                            Toast.makeText(this@DetectorActivity,
                                response.body()!!.statusCode,
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@DetectorActivity,
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SetDataWajahResponse>, t: Throwable) {
                    Log.e("data", "onFailure: " + t.message)
                    Toast.makeText(this@DetectorActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun onFacesDetected(currTimestamp: Long, faces: List<Face>, add: Boolean) {
        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap!!)
        Canvas(cropCopyBitmap!!)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f
        var minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API
        minimumConfidence =
            when (MODE) {
                DetectorMode.TF_OD_API -> MINIMUM_CONFIDENCE_TF_OD_API
            }

        val mappedRecognitions: MutableList<Recognition> = LinkedList()

        // Note this can be done only once
        val sourceW = rgbFrameBitmap!!.width
        val sourceH = rgbFrameBitmap!!.height
        val targetW = portraitBmp!!.width
        val targetH = portraitBmp!!.height
        val transform : Matrix = createTransform(
            sourceW,
            sourceH,
            targetW,
            targetH,
            sensorOrientation!!)
        val cv = Canvas(portraitBmp!!)

        // draws the original image in portrait mode.
        cv.drawBitmap(rgbFrameBitmap!!, transform, null)

        val cvFace = Canvas(faceBmp!!)
        for (face : Face  in faces) {
            LOGGER.i("FACE$face")
            LOGGER.i("Running detection on face $currTimestamp")
            //results = detector.recognizeImage(croppedBitmap);
            val boundingBox = RectF(face.boundingBox)

            //final boolean goodConfidence = result.getConfidence() >= minimumConfidence;
            val goodConfidence = true //face.get;
            if (goodConfidence) {

                // maps crop coordinates to original
                cropToFrameTransform!!.mapRect(boundingBox)

                // maps original coordinates to portrait coordinates
                val faceBB = RectF(boundingBox)
                transform.mapRect(faceBB)

                // translates portrait to origin and scales to fit input inference size
                //cv.drawRect(faceBB, paint);
                val sx = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.width()
                val sy = TF_OD_API_INPUT_SIZE.toFloat() / faceBB.height()
                val matrix = Matrix()
                matrix.postTranslate(-faceBB.left, -faceBB.top)
                matrix.postScale(sx, sy)
                cvFace.drawBitmap(portraitBmp!!, matrix, null)

                //canvas.drawRect(faceBB, paint);
                var label = ""
                var confidence = -1f
                var color = Color.BLUE
                var extra: Any? = null
                var crop: Bitmap? = null
                if (add) {
                    crop = Bitmap.createBitmap(portraitBmp!!,
                        faceBB.left.toInt(),
                        faceBB.top.toInt(),
                        faceBB.width().toInt(),
                        faceBB.height().toInt())
                }
                val startTime = SystemClock.uptimeMillis()
                val resultsAux = detector!!.recognizeImage(faceBmp, add)
                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime
                if (resultsAux.isNotEmpty()) {
                    val result = resultsAux[0]
                    extra = result?.extra

                    val conf = result?.distance
                    if (conf!! < 1.0f) {
                        confidence = conf
                        label = result.title!!
                        color = if (result.id == "0") {
                            Color.GREEN
                        } else {
                            Color.RED
                        }
                    }
                }
                if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {

                    // camera is frontal so the image is flipped horizontally
                    // flips horizontally
                    val flip = Matrix()
                    if (sensorOrientation == 90 || sensorOrientation == 270) {
                        flip.postScale(1f, -1f, previewWidth / 2.0f, previewHeight / 2.0f)
                    } else {
                        flip.postScale(-1f, 1f, previewWidth / 2.0f, previewHeight / 2.0f)
                    }
                    //flip.postScale(1, -1, targetW / 2.0f, targetH / 2.0f);
                    flip.mapRect(boundingBox)
                }
                val result = Recognition(
                    "0", label, confidence, boundingBox)
                result.color = color
                result.location = boundingBox
                result.extra = extra
                result.crop = crop
                mappedRecognitions.add(result)
            }
        }

        updateResults(currTimestamp, mappedRecognitions)

    }

    companion object {
        private val LOGGER = Logger()

        // FaceNet
        //  private static final int TF_OD_API_INPUT_SIZE = 160;
        //  private static final boolean TF_OD_API_IS_QUANTIZED = false;
        //  private static final String TF_OD_API_MODEL_FILE = "facenet.tflite";
        //  //private static final String TF_OD_API_MODEL_FILE = "facenet_hiroki.tflite";

        // MobileFaceNet
        private const val TF_OD_API_INPUT_SIZE = 112
        private const val TF_OD_API_IS_QUANTIZED = false
        private const val TF_OD_API_MODEL_FILE = "mobile_face_net.tflite"
        private const val TF_OD_API_LABELS_FILE = "file:///android_asset/labelmap.txt"
        private val MODE = DetectorMode.TF_OD_API

        // Minimum detection confidence to track a detection.
        private const val MINIMUM_CONFIDENCE_TF_OD_API = 0.5f
        private const val MAINTAIN_ASPECT = false
        private val DESIRED_PREVIEW_SIZE = Size(640, 480)

        //private static final int CROP_SIZE = 320;
        //private static final Size CROP_SIZE = new Size(320, 320);
        private const val SAVE_PREVIEW_BITMAP = false
        private const val TEXT_SIZE_DIP = 10f
    }
}