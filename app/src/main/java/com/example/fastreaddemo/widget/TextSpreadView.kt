package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.example.fastreaddemo.ext.dp

/**
 * 文字四周扩散
 * @author wzh
 * @date 2023/8/16
 */
class TextSpreadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomAnimationView(context, attrs) {

    private var centerX: Int = 0
    private var centerY: Int = 0

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 13f.dp
        color = Color.WHITE
        textAlign = Align.CENTER
    }

    private var text = "恭喜发财"

    private var textWidth: Float = 0f

    private var pointList = mutableListOf<PointF>()

    private var pathX = 0f
    private var pathY = 0f

    fun setText(text: String) {
        this.text = text
        textWidth = textPaint.measureText(text)
        invalidate()
    }

    override fun onAnimationFinish() {
        updatePoints(0f, 0f)
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

    override fun onAnimationRunning(fraction: Float) {
        updatePoints(pathX * fraction, pathY * fraction)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        centerX = w / 2
        centerY = h / 2
        textWidth = textPaint.measureText(text)

        pathX = centerX - (textWidth + 20f.dp) / 2
        pathY = centerY - 20f.dp

        pointList.clear()
        for (i in 1..4) {
            pointList.add(PointF())
        }

        updatePoints(0f, 0f)
    }

    private fun updatePoints(pathOffsetX: Float, pathOffsetY: Float) {
        for ((index, point) in pointList.withIndex()) {
            when (index) {
                0 -> {
                    point.x = centerX - (textWidth + 20f.dp) / 2 - pathOffsetX
                    point.y = centerY - 20f.dp - pathOffsetY
                }

                1 -> {
                    point.x = centerX + (textWidth + 20f.dp) / 2 + pathOffsetX
                    point.y = centerY - 20f.dp - pathOffsetY
                }

                2 -> {
                    point.x = centerX - (textWidth + 20f.dp) / 2 - pathOffsetX
                    point.y = centerY + 20f.dp + pathOffsetY
                }

                3 -> {
                    point.x = centerX + (textWidth + 20f.dp) / 2 + pathOffsetX
                    point.y = centerY + 20f.dp + pathOffsetY
                }
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pointList.forEach {
            canvas.drawText(text, it.x, it.y, textPaint)
        }

    }
}