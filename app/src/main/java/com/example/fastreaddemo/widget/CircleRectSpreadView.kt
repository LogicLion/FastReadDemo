package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp

/**
 * 圆环扩散/方框扩散
 */
class CircleRectSpreadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    CustomFrequencyAnimationView(context, attrs) {
    private val radiusList = mutableListOf<Float>()
    var isRect = false

    private var currRadius = -1f
    private val focusPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5CA2FF")
        style = Paint.Style.STROKE
        strokeWidth = 2f.dp
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 1f.dp
    }


    override fun onFrequencyUpdate(progress: Int) {
        currRadius = radiusList[progress]
        invalidate()
    }

    override fun getFrequencySize() = radiusList.size

    override fun onAnimationFinish() {
        super.onAnimationFinish()
        currRadius = -1f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpec = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(heightSpec, heightSpec)
        } else {
            setMeasuredDimension(widthSpec, widthSpec)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        computeRadius(w, h)
    }



    private fun computeRadius(w: Int, h: Int) {
        val minRadius = 50f.dp
        val maxRadius = w / 2 - 1f.dp
        for (i in 0..6) {
            radiusList.add(minRadius + i * (maxRadius - minRadius) / 6)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2


        if (isRect) {
            radiusList.forEach {
                canvas.drawRect(centerX - it, centerY - it, centerX + it, centerY + it, paint)
            }

            if (currRadius > 0) {
                canvas.drawRect(
                    centerX - currRadius,
                    centerY - currRadius,
                    centerX + currRadius,
                    centerY + currRadius,
                    focusPaint
                )
            }
        } else {
            radiusList.forEach {
                canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, it, paint)
            }

            if (currRadius > 0) {
                canvas.drawCircle(
                    width.toFloat() / 2,
                    height.toFloat() / 2,
                    currRadius,
                    focusPaint
                )
            }
        }

    }


}