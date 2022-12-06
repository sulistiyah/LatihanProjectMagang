package com.magang.projectmaganglatihan.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.magang.projectmaganglatihan.tflite.SimilarityClassifier.Recognition

class RecognitionScoreView(context: Context?, set: AttributeSet?) : View(context, set), ResultsView {
    private val textSizePx: Float
    private val fgPaint: Paint
    private val bgPaint: Paint
    private var results: List<Recognition>? = null
//    private lateinit var results: List<Recognition>

    init {
        textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, resources.displayMetrics)
        fgPaint = Paint()
        fgPaint.textSize = textSizePx
        bgPaint = Paint()
        bgPaint.color = -0x33bd7a0c
    }


    override fun setResults(results: List<Recognition?>?) {
        this.results = results as List<Recognition>?
        postInvalidate()

    }
//    fun setResults(results: List<Recognition>) {
//        this.results = results
//        postInvalidate()
//    }




    public override fun onDraw(canvas: Canvas) {
        val x = 10
        var y = (fgPaint.textSize * 1.5f).toInt()
        canvas.drawPaint(bgPaint)
        if (results != null) {
            for (recog in results!!) {
                canvas.drawText(recog.title + ": " + recog.distance,
                    x.toFloat(),
                    y.toFloat(),
                    fgPaint)
                y += (fgPaint.textSize * 1.5f).toInt()
            }
        }
    }

    companion object {
        private const val TEXT_SIZE_DIP = 14f
    }


}