package com.example.fastreaddemo.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp

/**
 * 圆周扩大/矩形扩大
 * @author wzh
 * @date 2023/6/15
 */
class EnlargeCircleRectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    CustomAnimationView(context, attrs) {

    private val initRadius = 5f.dp
    private var mostRadius = 0f
    private var radius = initRadius
    private var centerX = 0f
    private var centerY = 0f
    var isRect = false

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        strokeWidth = 2f.dp
        style = Paint.Style.STROKE
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

        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            mostRadius = (w - 20f.dp) / 2
            centerX = w.toFloat() / 2
            centerY = w.toFloat() / 2
        } else {
            mostRadius = (h - 20f.dp) / 2
            centerX = w.toFloat() / 2
            centerY = h.toFloat() / 2
        }

    }

    override fun onAnimationRunning(fraction: Float) {
        radius = (mostRadius - initRadius) * fraction + initRadius
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isRect) {
            canvas.drawRect(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius,
                paint
            )
        } else {
            canvas.drawCircle(centerX, centerY, radius, paint)
        }
    }
}