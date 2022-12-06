package com.magang.projectmaganglatihan.customview

import android.content.Context
import android.util.AttributeSet
import kotlin.jvm.JvmOverloads
import android.view.TextureView
import android.view.View.MeasureSpec

class AutoFitTextureView (
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextureView(
    context!!, attrs, defStyle) {
    private var ratioWidth = 0
    private var ratioHeight = 0

    fun setAspectRatio(width: Int, height: Int) {
        require(!(width < 0 || height < 0)) { "Size cannot be negative." }
        ratioWidth = width
        ratioHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (0 == ratioWidth || 0 == ratioHeight) {
            setMeasuredDimension(width, height)
        } else {
            if (width < height * ratioWidth / ratioHeight) {
                setMeasuredDimension(width, width * ratioHeight / ratioWidth)
            } else {
                setMeasuredDimension(height * ratioWidth / ratioHeight, height)
            }
        }
    }
}