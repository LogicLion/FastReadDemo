package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 圆圈顺向移动/圆圈逆向移动
 * @author wzh
 * @date 2023/5/15
 */
class CirclePathView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    CustomAnimationView(context, attrs) {

    private var bigCircleRadius = 200f
    private val ballRadius = 17f.dp

    /**
     * true：顺时针，false：逆时针
     */
    var isClockWise = true


    //旋转角度
    private var rotationAngle: Float = 0f

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private val bigCirclePaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 1f.dp
    }

    private val strokeColor = Color.parseColor("#616161")
    private val ballPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }


    override fun onAnimationRunning(fraction: Float) {
        rotationAngle = if (isClockWise) {
            360f * fraction
        } else {
            -360f * fraction
        }
        invalidate()
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

        bigCircleRadius = (w - 50f.dp) / 2
        centerX = w.toFloat() / 2
        centerY = h.toFloat() / 2
    }


    override fun onDraw(canvas: Canvas) {

        canvas.drawCircle(centerX, centerY, bigCircleRadius, bigCirclePaint)
        val smallCircleX =
            centerX + bigCircleRadius * cos(Math.toRadians(rotationAngle.toDouble())).toFloat()
        val smallCircleY =
            centerY + bigCircleRadius * sin(Math.toRadians(rotationAngle.toDouble())).toFloat()

        ballPaint.color = strokeColor
        canvas.drawCircle(smallCircleX, smallCircleY, ballRadius, ballPaint)

        ballPaint.color = Color.WHITE
        canvas.drawCircle(smallCircleX, smallCircleY, ballRadius - 4f.dp, ballPaint)
    }
}