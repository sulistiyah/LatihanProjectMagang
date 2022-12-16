package com.magang.projectmaganglatihan.tflite

import android.graphics.Bitmap
import android.graphics.RectF

/** Generic interface for interacting with different recognition engines.  */
interface SimilarityClassifier {

    fun register(recognition: Recognition?)
    fun recognizeImage(bitmap: Bitmap?, getExtra: Boolean): List<Recognition?>
    fun enableStatLogging(debug: Boolean?)
    val statString: String?

    fun close()
//    fun setNumThreads(num_threads: Int)
//    fun setUseNNAPI(isChecked: Boolean)

    /** An immutable result returned by a Classifier describing what was recognized.  */
    class Recognition(
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        val id: String?,
        /** Display name for the recognition.  */
        var title: String?,
        distance: Float?,
        location: RectF?,
    ) {

        /**
         * A sortable score for how good the recognition is relative to others. Lower should be better.
         */
        val distance: Float?
        var extra: Any?

        /** Optional location within the source image for the location of the recognized object.  */
        private var location: RectF?
        var color: Int?
        var crop: Bitmap?

        init {
            title = title
            this.distance = distance
            this.location = location
            color = null
            extra = null
            crop = null
        }

        fun getLocation(): RectF {
            return RectF(location)
        }

        fun setLocation(location: RectF?) {
            this.location = location
        }

        override fun toString(): String {
            var resultString = ""
            if (id != null) {
                resultString += "[$id] "
            }
            if (title != null) {
                resultString += "$title "
            }
            if (distance != null) {
                resultString += String.format("(%.1f%%) ", distance * 100.0f)
            }
            if (location != null) {
                resultString += location.toString() + " "
            }
            return resultString.trim { it <= ' ' }
        }
    }
}