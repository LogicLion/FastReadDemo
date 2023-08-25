package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp
import kotlin.math.min

/**
 * 之字移动、对角线移动
 * @author wzh
 * @date 2023/5/10
 */
class BallLinePathView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomAnimationView(context, attrs) {

    private val ballRadius = 25f.dp

    var connectLineType: BallConnectLineType = BallConnectLineType.ZHI_ZI_LINE

    private val ballColor = Color.parseColor("#5CA2FF")
    private val defaultBallStrokeColor = Color.parseColor("#616161")
    private val ballStrokeColor = Color.parseColor("#29577A")

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    /**
     * 之字移动：false：横向，true：竖向
     * 对角线移动：false：顺向，true：逆向
     */
    var direct = true

    private val pointsList = mutableListOf<PointF>()


    private var pathLength = 0f
    private var pathLengthProgress = 0f

    private val ballPosition = FloatArray(2)

    private val pathPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 0.5f.dp

        val dashLength = 4f.dp // 虚线的线段长度
        val gapLength = 7f.dp // 虚线的间隔长度

        val dashEffect = DashPathEffect(floatArrayOf(dashLength, gapLength), 0f)

        pathEffect = dashEffect
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

        pointsList.forEach {
            ballPaint.color = defaultBallStrokeColor
            canvas.drawCircle(it.x, it.y, ballRadius, ballPaint)

            ballPaint.color = Color.WHITE
            canvas.drawCircle(
                it.x, it.y, ballRadius - 8f.dp, ballPaint
            )
        }

        ballPaint.color = ballStrokeColor
        canvas.drawCircle(ballPosition[0], ballPosition[1], ballRadius, ballPaint)

        ballPaint.color = ballColor
        canvas.drawCircle(
            ballPosition[0], ballPosition[1], ballRadius - 8f.dp, ballPaint
        )
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = resolveSize(530.dp, widthMeasureSpec)
        val heightSize = resolveSize(430.dp, heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }


    override fun onAnimationRunning(fraction: Float) {
        pathLengthProgress = pathLength * fraction
        pathMeasure.getPosTan(pathLengthProgress, ballPosition, null)
        invalidate()
    }


    private fun createPath(w: Int, h: Int) {
        when (connectLineType) {
            BallConnectLineType.ZHI_ZI_LINE -> {
                if (direct) {
                    createZhiZiVerticalPath(w, h)
                } else {
                    createZhiZiHorizontalPath(w, h)
                }
            }

            BallConnectLineType.DIAGONAL_LINE -> {
                if (direct) {
                    createDiagonalReversePath(w, h)
                } else {
                    createDiagonalPath(w, h)
                }
            }
        }


    }

    private fun createZhiZiHorizontalPath(w: Int, h: Int) {
        path.reset()


        val verticalSpace = (h - 50f.dp) / 4
        for (i in 0..9) {
            if (i % 2 == 0) {
                //左
                pointsList.add(PointF(ballRadius, verticalSpace * (i / 2) + ballRadius))
            } else {
                //右
                pointsList.add(PointF(w - ballRadius, verticalSpace * (i / 2) + ballRadius))
            }
        }

        for ((index, point) in pointsList.withIndex()) {
            if (index == 0) {
                path.moveTo(point.x, point.y)
                ballPosition[0] = point.x
                ballPosition[1] = point.y
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        for (index in pointsList.size - 2 downTo 0) {
            path.lineTo(pointsList[index].x, pointsList[index].y)
        }

        path.close()

    }


    private fun createZhiZiVerticalPath(w: Int, h: Int) {
        path.reset()

        val width = w - 50f.dp

        val horizontalSpace = width / 4
        for (i in 0..9) {
            if (i % 2 == 0) {
                //下
                pointsList.add(
                    PointF(
                        horizontalSpace * (i / 2) + 25f.dp,
                        h - 50f.dp
                    )
                )
            } else {
                //上
                pointsList.add(PointF(horizontalSpace * (i / 2) + 25f.dp, 50f.dp))
            }
        }

        for ((index, point) in pointsList.withIndex()) {
            if (index == 0) {
                path.moveTo(point.x, point.y)
                ballPosition[0] = point.x
                ballPosition[1] = point.y
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        for (index in pointsList.size - 2 downTo 0) {
            path.lineTo(pointsList[index].x, pointsList[index].y)
        }

        path.close()
    }


    /**
     * 对角线顺向
     */
    private fun createDiagonalPath(w: Int, h: Int) {
        path.reset()
        val mostWidth = min(500.dp, w)
        val mostHeight = min(500.dp, h)

        val margin = (width - mostWidth) / 2
        val verticalSpace = mostHeight - 50f.dp
        for (i in 0..3) {
            if (i % 2 == 0) {
                //左
                pointsList.add(PointF(25f.dp + margin, verticalSpace * (i / 2) + 25f.dp))
            } else {
                //右
                pointsList.add(PointF(width - 25f.dp - margin, verticalSpace * (i / 2) + 25f.dp))
            }
        }

        for ((index, point) in pointsList.withIndex()) {
            if (index == 0) {
                path.moveTo(point.x, point.y)
                ballPosition[0] = point.x
                ballPosition[1] = point.y
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        path.close()
    }

    /**
     * 对角线逆向
     */
    private fun createDiagonalReversePath(w: Int, h: Int) {
        path.reset()
        val mostWidth = min(500.dp, w)
        val mostHeight = min(500.dp, h)
        val margin = (width - mostWidth) / 2
        val verticalSpace = mostHeight - 50f.dp
        for (i in 0..3) {
            if (i % 2 == 0) {
                //右
                pointsList.add(PointF(25f.dp + margin, verticalSpace * (i / 2) + 25f.dp))
            } else {
                //左
                pointsList.add(PointF(width - 25f.dp - margin, verticalSpace * (i / 2) + 25f.dp))
            }
        }

        for ((index, point) in pointsList.withIndex()) {
            if (index == 0) {
                path.moveTo(point.x, point.y)
                ballPosition[0] = point.x
                ballPosition[1] = point.y
            } else {
                path.lineTo(point.x, point.y)
            }
        }

        path.close()

    }


}