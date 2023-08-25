package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author wzh
 * @date 2023/5/10
 */
class StarPathView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomAnimationView(context, attrs) {


    private val ballRadius = 17f.dp

    private val strokeColor = Color.parseColor("#616161")

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#492d22")
        style = Paint.Style.FILL
    }

    private var pathLength = 0f
    private var pathLengthProgress = 0f

    private val ballPosition = FloatArray(2)

    private var centerX: Float = 0f
    private var centerY: Float = 0f


    private val pathPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 1f.dp
    }


    private val pathMeasure: PathMeasure by lazy {
        PathMeasure(path, true)
    }

    private val path = Path().apply {

    }

    override fun onAnimationRunning(fraction: Float) {
        pathLengthProgress = pathLength * fraction
        pathMeasure.getPosTan(pathLengthProgress, ballPosition, null)
        invalidate()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        createPath(w, h)
        pathLength = pathMeasure.length
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制五角星
        canvas.drawPath(path, pathPaint)

        ballPaint.color = strokeColor
        canvas.drawCircle(ballPosition[0], ballPosition[1], ballRadius, ballPaint)

        ballPaint.color = Color.WHITE
        canvas.drawCircle(
            ballPosition[0],
            ballPosition[1],
            ballRadius - 4f.dp,
            ballPaint
        )
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


    private fun createPath(w: Int, h: Int) {
        val radius = (h - 25.dp) / 2
        val centerX = w / 2
        val centerY = h / 2

        // 构建五角星路径
        path.reset()


        val angleList = listOf(270, 126, 342, 198, 54)

        for ((index, angle) in angleList.withIndex()) {

            val x = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

            if (index == 0) {
                path.moveTo(x, y) // 移动到起始点
                ballPosition[0] = x
                ballPosition[1] = y
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
    }
}