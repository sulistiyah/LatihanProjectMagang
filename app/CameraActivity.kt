package com.magang.projectmaganglatihan.activity

import com.magang.projectmaganglatihan.env.ImageUtils
import com.magang.projectmaganglatihan.env.Logger

abstract class CameraActivity : AppCompatActivity(),
    android.media.ImageReader.OnImageAvailableListener,
    PreviewCallback,
    CompoundButton.OnCheckedChangeListener,
    android.view.View.OnClickListener {


    protected var previewWidth = 0
    protected var previewHeight = 0
    val isDebug = false
    private var handler: android.os.Handler? = null
    private var handlerThread: HandlerThread? = null
    private var useCamera2API = false
    private var isProcessingFrame = false
    private val yuvBytes = arrayOfNulls<ByteArray>(3)
    private var rgbBytes: IntArray? = null
    protected var luminanceStride = 0
    private var postInferenceCallback: Runnable? = null
    private var imageConverter: Runnable? = null
    private var bottomSheetLayout: LinearLayout? = null
    private var gestureLayout: LinearLayout? = null
    private var sheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    protected var frameValueTextView: TextView? = null
    protected var cropValueTextView: TextView? = null
    protected var inferenceTimeTextView: TextView? = null
    protected var bottomSheetArrowImageView: ImageView? = null
    private var plusImageView: ImageView? = null
    private var minusImageView: ImageView? = null
    private var apiSwitchCompat: SwitchCompat? = null
    private var threadsTextView: TextView? = null
    private var btnSwitchCam: FloatingActionButton? = null
    protected var cameraFacing: Int? = null
    private var cameraId: String? = null


    protected override fun onCreate(savedInstanceState: Bundle?) {
        LOGGER.d("onCreate $this")
        super.onCreate(null)
        val intent: Intent = getIntent()
        //useFacing = intent.getIntExtra(KEY_USE_FACING, CameraCharacteristics.LENS_FACING_FRONT);
        cameraFacing = intent.getIntExtra(KEY_USE_FACING, CameraCharacteristics.LENS_FACING_BACK)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_camera)
        val toolbar: androidx.appcompat.widget.Toolbar =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar().setDisplayShowTitleEnabled(false)
        if (hasPermission()) {
            setFragment()
        } else {
            requestPermission()
        }
        threadsTextView = findViewById<TextView>(R.id.threads)
        plusImageView = findViewById<ImageView>(R.id.plus)
        minusImageView = findViewById<ImageView>(R.id.minus)
        apiSwitchCompat = findViewById<SwitchCompat>(R.id.api_info_switch)
        bottomSheetLayout = findViewById<LinearLayout>(R.id.bottom_sheet_layout)
        gestureLayout = findViewById<LinearLayout>(R.id.gesture_layout)
        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottomSheetLayout)
        bottomSheetArrowImageView = findViewById<ImageView>(R.id.bottom_sheet_arrow)
        btnSwitchCam = findViewById<FloatingActionButton>(R.id.fab_switchcam)
        val vto: ViewTreeObserver = gestureLayout.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(
            object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        gestureLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                    } else {
                        gestureLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                    }
                    //                int width = bottomSheetLayout.getMeasuredWidth();
                    val height: Int = gestureLayout.getMeasuredHeight()
                    sheetBehavior.setPeekHeight(height)
                }
            })
        sheetBehavior.setHideable(false)
        sheetBehavior.setBottomSheetCallback(
            object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: android.view.View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {}
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_down)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            bottomSheetArrowImageView.setImageResource(R.drawable.icn_chevron_up)
                        }
                        BottomSheetBehavior.STATE_DRAGGING -> {}
                        BottomSheetBehavior.STATE_SETTLING -> bottomSheetArrowImageView.setImageResource(
                            R.drawable.icn_chevron_up
                        )
                    }
                }

                override fun onSlide(bottomSheet: android.view.View, slideOffset: Float) {}
            })
        frameValueTextView = findViewById<TextView>(R.id.frame_info)
        cropValueTextView = findViewById<TextView>(R.id.crop_info)
        inferenceTimeTextView = findViewById<TextView>(R.id.inference_info)
        apiSwitchCompat.setOnCheckedChangeListener(this)
        plusImageView.setOnClickListener(this)
        minusImageView.setOnClickListener(this)
        btnSwitchCam.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(v: android.view.View) {
                onSwitchCamClick()
            }
        })
    }

    private fun onSwitchCamClick() {
        switchCamera()
    }

    fun switchCamera() {
        val intent: Intent = getIntent()
        if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
            cameraFacing = CameraCharacteristics.LENS_FACING_BACK
        } else {
            cameraFacing = CameraCharacteristics.LENS_FACING_FRONT
        }
        intent.putExtra(
            KEY_USE_FACING,
            cameraFacing
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        restartWith(intent)
    }

    private fun restartWith(intent: Intent) {
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    protected fun getRgbBytes(): IntArray? {
        imageConverter.run()
        return rgbBytes
    }

    protected val luminance: ByteArray?
        protected get() = yuvBytes[0]

    /** Callback for android.hardware.Camera API  */
    override fun onPreviewFrame(bytes: ByteArray, camera: android.hardware.Camera) {
        if (isProcessingFrame) {
            LOGGER.w("Dropping frame!")
            return
        }
        try {
            // Initialize the storage bitmaps once when the resolution is known.
            if (rgbBytes == null) {
                val previewSize: android.hardware.Camera.Size =
                    camera.getParameters().getPreviewSize()
                previewHeight = previewSize.height
                previewWidth = previewSize.width
                //rgbBytes = new int[previewWidth * previewHeight];
                //onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 90);
                rgbBytes = IntArray(previewWidth * previewHeight)
                var rotation = 90
                if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    rotation = 270
                }
                onPreviewSizeChosen(
                    android.util.Size(previewSize.width, previewSize.height),
                    rotation
                )
            }
        } catch (e: Exception) {
            LOGGER.e(e, "Exception!")
            return
        }
        isProcessingFrame = true
        yuvBytes[0] = bytes
        luminanceStride = previewWidth
        imageConverter = object : Runnable {
            override fun run() {
                ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes)
            }
        }
        postInferenceCallback = object : Runnable {
            override fun run() {
                camera.addCallbackBuffer(bytes)
                isProcessingFrame = false
            }
        }
        processImage()
    }

    /** Callback for Camera2 API  */
    override fun onImageAvailable(reader: android.media.ImageReader) {
        // We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return
        }
        if (rgbBytes == null) {
            rgbBytes = IntArray(previewWidth * previewHeight)
        }
        try {
            val image: Image = reader.acquireLatestImage() ?: return
            if (isProcessingFrame) {
                image.close()
                return
            }
            isProcessingFrame = true
            android.os.Trace.beginSection("imageAvailable")
            val planes: Array<Plane> = image.getPlanes()
            fillBytes(planes, yuvBytes)
            luminanceStride = planes[0].getRowStride()
            val uvRowStride: Int = planes[1].getRowStride()
            val uvPixelStride: Int = planes[1].getPixelStride()
            imageConverter = object : Runnable {
                override fun run() {
                    ImageUtils.convertYUV420ToARGB8888(
                        yuvBytes[0],
                        yuvBytes[1],
                        yuvBytes[2],
                        previewWidth,
                        previewHeight,
                        luminanceStride,
                        uvRowStride,
                        uvPixelStride,
                        rgbBytes
                    )
                }
            }
            postInferenceCallback = object : Runnable {
                override fun run() {
                    image.close()
                    isProcessingFrame = false
                }
            }
            processImage()
        } catch (e: Exception) {
            LOGGER.e(e, "Exception!")
            android.os.Trace.endSection()
            return
        }
        android.os.Trace.endSection()
    }

    @kotlin.jvm.Synchronized
    override fun onStart() {
        LOGGER.d("onStart $this")
        super.onStart()
    }

    @kotlin.jvm.Synchronized
    override fun onResume() {
        LOGGER.d("onResume $this")
        super.onResume()
        handlerThread = HandlerThread("inference")
        handlerThread.start()
        handler = android.os.Handler(handlerThread.getLooper())
    }

    @kotlin.jvm.Synchronized
    override fun onPause() {
        LOGGER.d("onPause $this")
        handlerThread.quitSafely()
        try {
            handlerThread.join()
            handlerThread = null
            handler = null
        } catch (e: InterruptedException) {
            LOGGER.e(e, "Exception!")
        }
        super.onPause()
    }

    @kotlin.jvm.Synchronized
    override fun onStop() {
        LOGGER.d("onStop $this")
        super.onStop()
    }

    @kotlin.jvm.Synchronized
    override fun onDestroy() {
        LOGGER.d("onDestroy $this")
        super.onDestroy()
    }

    @kotlin.jvm.Synchronized
    protected fun runInBackground(r: Runnable?) {
        if (handler != null) {
            handler.post(r)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {
                setFragment()
            } else {
                requestPermission()
            }
        }
    }

    private fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
                Toast.makeText(
                    this@CameraActivity,
                    "Camera permission is required for this demo",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            requestPermissions(arrayOf(PERMISSION_CAMERA), PERMISSIONS_REQUEST)
        }
    }

    // Returns true if the device supports the required hardware level, or better.
    private fun isHardwareLevelSupported(
        characteristics: CameraCharacteristics, requiredLevel: Int
    ): Boolean {
        val deviceLevel: Int =
            characteristics.get<Int>(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        return if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            requiredLevel == deviceLevel
        } else requiredLevel <= deviceLevel
        // deviceLevel is not LEGACY, can use numerical sort
    }

    private fun chooseCamera(): String? {
        val manager: CameraManager =
            getSystemService(android.content.Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.getCameraIdList()) {
                val characteristics: CameraCharacteristics =
                    manager.getCameraCharacteristics(cameraId)
                val map: StreamConfigurationMap =
                    characteristics.get<StreamConfigurationMap>(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        ?: continue

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                //final int facing =
                //(facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
//        if (!facing.equals(useFacing)) {
//          continue;
//        }
                val facing: Int = characteristics.get<Int>(CameraCharacteristics.LENS_FACING)
                if (cameraFacing != null && facing != null &&
                    facing != cameraFacing
                ) {
                    continue
                }
                useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL
                        || isHardwareLevelSupported(
                    characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL
                ))
                LOGGER.i("Camera API lv2?: %s", useCamera2API)
                return cameraId
            }
        } catch (e: CameraAccessException) {
            LOGGER.e(e, "Not allowed to access camera")
        }
        return null
    }

    protected fun setFragment() {
        cameraId = chooseCamera()
        val fragment: android.app.Fragment
        if (useCamera2API) {
            val camera2Fragment: CameraConnectionFragment = CameraConnectionFragment.newInstance(
                object : ConnectionCallback() {
                    fun onPreviewSizeChosen(size: android.util.Size, rotation: Int) {
                        previewHeight = size.getHeight()
                        previewWidth = size.getWidth()
                        this@CameraActivity.onPreviewSizeChosen(size, rotation)
                    }
                },
                this,
                layoutId,
                desiredPreviewFrameSize
            )
            camera2Fragment.setCamera(cameraId)
            fragment = camera2Fragment
        } else {
            val facing: Int =
                if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK else android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
            val frag = LegacyCameraConnectionFragment(
                this,
                layoutId,
                desiredPreviewFrameSize, facing
            )
            fragment = frag
        }
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit()
    }

    protected fun fillBytes(planes: Array<Plane>, yuvBytes: Array<ByteArray?>) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (i in planes.indices) {
            val buffer: ByteBuffer = planes[i].getBuffer()
            if (yuvBytes[i] == null) {
                LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity())
                yuvBytes[i] = ByteArray(buffer.capacity())
            }
            buffer.get(yuvBytes[i])
        }
    }

    protected fun readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback.run()
        }
    }

    protected val screenOrientation: Int
        protected get() = when (getWindowManager().getDefaultDisplay().getRotation()) {
            android.view.Surface.ROTATION_270 -> 270
            android.view.Surface.ROTATION_180 -> 180
            android.view.Surface.ROTATION_90 -> 90
            else -> 0
        }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        setUseNNAPI(isChecked)
        if (isChecked) apiSwitchCompat.setText("NNAPI") else apiSwitchCompat.setText("TFLITE")
    }

    override fun onClick(v: android.view.View) {
        if (v.getId() == R.id.plus) {
            val threads: String = threadsTextView.getText().toString().trim { it <= ' ' }
            var numThreads: Int = threads.toInt()
            if (numThreads >= 9) return
            numThreads++
            threadsTextView.setText(numThreads.toString())
            setNumThreads(numThreads)
        } else if (v.getId() == R.id.minus) {
            val threads: String = threadsTextView.getText().toString().trim { it <= ' ' }
            var numThreads: Int = threads.toInt()
            if (numThreads == 1) {
                return
            }
            numThreads--
            threadsTextView.setText(numThreads.toString())
            setNumThreads(numThreads)
        }
    }

    protected fun showFrameInfo(frameInfo: String?) {
        frameValueTextView.setText(frameInfo)
    }

    protected fun showCropInfo(cropInfo: String?) {
        cropValueTextView.setText(cropInfo)
    }

    protected fun showInference(inferenceTime: String?) {
        inferenceTimeTextView.setText(inferenceTime)
    }

    protected abstract fun processImage()
    protected abstract fun onPreviewSizeChosen(size: android.util.Size?, rotation: Int)
    protected abstract val layoutId: Int
    protected abstract val desiredPreviewFrameSize: android.util.Size?

    protected abstract fun setNumThreads(numThreads: Int)
    protected abstract fun setUseNNAPI(isChecked: Boolean)

    companion object {
        private val LOGGER: Logger = Logger()
        private const val PERMISSIONS_REQUEST = 1
        private val PERMISSION_CAMERA: String = android.Manifest.permission.CAMERA
        private const val KEY_USE_FACING = "use_facing"
        private fun allPermissionsGranted(grantResults: IntArray): Boolean {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }
}