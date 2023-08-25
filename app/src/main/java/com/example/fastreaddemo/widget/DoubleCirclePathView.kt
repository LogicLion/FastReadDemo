package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp
import kotlin.math.min

/**
 * @author wzh
 * @date 2023/5/24
 */
class DoubleCirclePathView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomAnimationView(context, attrs) {
    private val ballRadius = 17f.dp

    val strokeColor = Color.parseColor("#616161")

    /**
     * true:横向8字，false:竖向8字
     */
    var isHorizontal = true

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#492d22")
        style = Paint.Style.FILL
    }

    private var pathLength = 0f
    private var pathLengthProgress = 0f
    private val ballPosition = FloatArray(2)

    private val pathPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 1f.dp
    }


    private val pathMeasure: PathMeasure by lazy {
        PathMeasure(path, true)
    }

    private val path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        createPath(w, h)
        pathLength = pathMeasure.length
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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

        if (isHorizontal) {
            if (widthMode != MeasureSpec.EXACTLY) {
                //横屏
                val specWidth = (heightSpec - ballRadius * 2) * 2 + ballRadius * 2
                setMeasuredDimension(specWidth.toInt(), heightSpec)
            } else {
                //竖屏
                val specHeight = (widthSpec - ballRadius * 2) / 2 + ballRadius * 2
                setMeasuredDimension(widthSpec, specHeight.toInt())
            }

        } else {

            if (widthMode != MeasureSpec.EXACTLY) {
                //横屏
                val specWidth = (heightSpec - ballRadius * 2) / 2 + ballRadius * 2
                setMeasuredDimension(specWidth.toInt(), heightSpec)
            } else {
                //竖屏
                val mostRadius = (heightSpec / 2 - ballRadius) / 2
                val radius = min(120f.dp, mostRadius)
                val specHeight = radius * 4 + ballRadius * 2
                setMeasuredDimension(widthSpec, specHeight.toInt())
            }

        }
    }


    override fun onAnimationRunning(fraction: Float) {
        pathLengthProgress = fraction * pathLength
        pathMeasure.getPosTan(pathLengthProgress, ballPosition, null)
        invalidate()
    }

    private fun createPath(width: Int, height: Int) {
        if (isHorizontal) {
            createHorizontalPath(width, height)
        } else {
            createVerticalPath(width, height)
        }
    }

    private fun createHorizontalPath(width: Int, height: Int) {

        val padding = 20f.dp

        val radius = height / 2 - padding

        val leftCircleLeft = width / 2 - radius * 2
        val leftCircleTop = padding
        val leftCircleRight = width.toFloat() / 2
        val leftCircleBottom = padding + radius * 2


        ballPosition[0] = width / 2.toFloat()
        ballPosition[1] = radius + padding
        path.moveTo(width / 2.toFloat(), radius + padding)
        path.arcTo(
            leftCircleLeft,
            leftCircleTop,
            leftCircleRight,
            leftCircleBottom,
            0f,
            -180f,
            false
        )
        path.lineTo(width / 2 - radius * 2, radius + padding)
        path.arcTo(
            leftCircleLeft,
            leftCircleTop,
            leftCircleRight,
            leftCircleBottom,
            180f,
            -180f,
            false
        )

        path.lineTo(width / 2.toFloat(), radius + padding)

        val rightCircleLeft = width.toFloat() / 2
        val rightCircleTop = padding
        val rightCircleRight = width / 2 + radius * 2
        val rightCircleBottom = radius * 2 + padding

        path.arcTo(
            rightCircleLeft,
            rightCircleTop,
            rightCircleRight,
            rightCircleBottom,
            180f,
            180f,
            false
        )
        path.lineTo(width / 2 + radius * 2, radius + padding)
        path.arcTo(
            rightCircleLeft,
            rightCircleTop,
            rightCircleRight,
            rightCircleBottom,
            0f,
            180f,
            false
        )

        path.close()

    }

    private fun createVerticalPath(width: Int, height: Int) {
        val padding = 20f.dp

        val mostRadius = (height / 2 - padding) / 2

        val radius = min(120f.dp, mostRadius)

        val topCircleLeft = width / 2 - radius
        val topCircleTop = padding
        val topCircleRight = width / 2 + radius
        val topCircleBottom = padding + radius * 2

        ballPosition[0] = width.toFloat() / 2
        ballPosition[1] = padding + radius * 2
        path.moveTo(width.toFloat() / 2, padding + radius * 2)
        path.arcTo(
            topCircleLeft,
            topCircleTop,
            topCircleRight,
            topCircleBottom,
            90f,
            180f,
            false
        )
        path.lineTo(width.toFloat() / 2, padding)
        path.arcTo(
            topCircleLeft,
            topCircleTop,
            topCircleRight,
            topCircleBottom,
            270f,
            180f,
            false
        )

        path.lineTo(width.toFloat() / 2, padding + radius * 2)

        val bottomCircleLeft = width / 2 - radius
        val bottomCircleTop = padding + radius * 2
        val bottomCircleRight = width / 2 + radius
        val bottomCircleBottom = padding + radius * 4

        path.arcTo(
            bottomCircleLeft,
            bottomCircleTop,
            bottomCircleRight,
            bottomCircleBottom,
            270f,
            -180f,
            false
        )
        path.lineTo(width.toFloat() / 2, padding + radius * 4)
        path.arcTo(
            bottomCircleLeft,
            bottomCircleTop,
            bottomCircleRight,
            bottomCircleBottom,
            90f,
            -180f,
            false
        )

        path.close()
    }
}