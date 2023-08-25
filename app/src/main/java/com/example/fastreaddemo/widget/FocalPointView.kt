package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp

/**
 * 凝视点训练
 */
class FocalPointView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomAnimationView(context, attrs) {

    private val ballRadius = 17f.dp
    private val strokeColor = Color.parseColor("#616161")

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 0.5f.dp
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resolveSize(200.dp, widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    override fun onAnimationRunning(fraction: Float) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        ballPaint.color = strokeColor
        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, ballRadius, ballPaint)

        ballPaint.color = Color.WHITE
        canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, ballRadius - 4f.dp, ballPaint)

        canvas.drawRect(0.5f.dp, 0.5f.dp, width - 0.5f.dp, height - 0.5f.dp, paint)
    }
}