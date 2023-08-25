package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp

/**
 * 对角线数字移动
 */
class DiagonalFlashView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomFrequencyAnimationView(context, attrs) {

    private val ballRadius = 25f.dp

    private val defaultPointsList = mutableListOf<PointF>()
    private val pointsList = mutableListOf<PointF>()

    private val ballColor = Color.parseColor("#5CA2FF")
    private val defaultBallStrokeColor = Color.parseColor("#616161")
    private val ballStrokeColor = Color.parseColor("#29577A")


    private var currPoint: PointF? = null

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }


    override fun onAnimationFinish() {
        super.onAnimationFinish()
        currPoint = null
    }

    override fun onFrequencyUpdate(progress: Int) {
        currPoint = pointsList[progress]
        invalidate()
    }

    override fun getFrequencySize() = pointsList.size


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = resolveSize(530.dp, widthMeasureSpec)
        val heightSize = resolveSize(430.dp, heightMeasureSpec)
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        createBalls(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        defaultPointsList.forEach {
            ballPaint.color = defaultBallStrokeColor
            canvas.drawCircle(it.x, it.y, ballRadius, ballPaint)

            ballPaint.color = Color.WHITE
            canvas.drawCircle(
                it.x,
                it.y,
                ballRadius - 8f.dp,
                ballPaint
            )
        }

        currPoint?.let {
            ballPaint.color = ballStrokeColor
            canvas.drawCircle(it.x, it.y, ballRadius, ballPaint)

            ballPaint.color = ballColor
            canvas.drawCircle(
                it.x,
                it.y,
                ballRadius - 8f.dp,
                ballPaint
            )
        }

    }

    private fun createBalls(w: Int, h: Int) {

        val centerW = w.toFloat() / 2
        val centerH = h.toFloat() / 2

        val halfCenterW = (w.toFloat() / 2 - ballRadius) / 2
        val halfCenterH = (h.toFloat() / 2 - ballRadius) / 2

        defaultPointsList.add(PointF(centerW, centerH))
        defaultPointsList.add(PointF(centerW + halfCenterW, centerH + halfCenterH))
        defaultPointsList.add(PointF(centerW - halfCenterW, centerH - halfCenterH))
        defaultPointsList.add(PointF(w - ballRadius, h - ballRadius))
        defaultPointsList.add(PointF(ballRadius, ballRadius))
        defaultPointsList.add(PointF(centerW - halfCenterW, centerH + halfCenterH))
        defaultPointsList.add(PointF(centerW + halfCenterW, centerH - halfCenterH))
        defaultPointsList.add(PointF(ballRadius, h - ballRadius))
        defaultPointsList.add(PointF(w - ballRadius, ballRadius))

        pointsList.addAll(defaultPointsList)
        pointsList.add(5, PointF(centerW, centerH))
    }
}