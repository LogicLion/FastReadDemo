package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * 箭头扩散
 * @author wzh
 * @date 2023/7/18
 */
class ArrowSpreadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomAnimationView(context, attrs) {
    private val ballColor = Color.parseColor("#5CA2FF")
    private val ballStrokeColor = Color.parseColor("#29577A")

    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f.dp
    }

    private val ballPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val arrowList = mutableListOf<Path>()

    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private val ballRadius = 17f.dp


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
        val arrowShaftLength = centerX

        for ((index, path) in arrowList.withIndex()) {
            val length = fraction * centerX * 2.5f

            val rotationAngle = index * 30

            val angle = Math.toRadians(rotationAngle.toDouble())
            val sin = sin(angle).toFloat()
            val cos = cos(angle).toFloat()
            if (length < arrowShaftLength) {
                val startX = centerX
                val startY = centerY
                val endX = centerX + length * cos
                val endY = centerY + length * sin
                updateArrowPath(path, startX, startY, endX, endY, rotationAngle)
            } else {
                val startX = centerX + (length - arrowShaftLength) * cos
                val startY = centerY + (length - arrowShaftLength) * sin
                val endX = startX + length * cos
                val endY = startY + length * sin
                updateArrowPath(path, startX, startY, endX, endY, rotationAngle)
            }

            invalidate()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

//        val currentOrientation = resources.configuration.orientation
//        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
//            centerX = w.toFloat() / 2
//            centerY = w.toFloat() / 2
//        } else {
//            centerX = w.toFloat() / 2
//            centerY = h.toFloat() / 2
//        }

        centerX = w.toFloat() / 2
        centerY = h.toFloat() / 2

        arrowList.clear()
        for (i in 0..11) {
            val path = Path()
            arrowList.add(path)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        arrowList.forEach {
            // 绘制箭头
            canvas.drawPath(it, arrowPaint)
        }

        ballPaint.color = ballStrokeColor
        canvas.drawCircle(centerX, centerY, ballRadius, ballPaint)

        ballPaint.color = ballColor
        canvas.drawCircle(
            centerX,
            centerY,
            ballRadius - 5f.dp,
            ballPaint
        )
        canvas.drawRect(1f.dp, 1f.dp, width - 1f.dp, height - 1f.dp, arrowPaint)
    }


    private fun updateArrowPath(
        arrowPath: Path, startX: Float, startY: Float, endX: Float, endY: Float, rotationAngle: Int
    ) {

        //箭嘴（投射到箭杆的）长度
        val arrowHeadLength = 5.dp

        // 箭头角度
        val angle = Math.toRadians(rotationAngle.toDouble())
        val sin = sin(angle).toFloat()
        val cos = cos(angle).toFloat()

        // 箭头的两边
        val side1X = endX - arrowHeadLength * cos + arrowHeadLength * sin
        val side1Y = endY - arrowHeadLength * sin - arrowHeadLength * cos
        val side2X = endX - arrowHeadLength * cos - arrowHeadLength * sin
        val side2Y = endY - arrowHeadLength * sin + arrowHeadLength * cos

        // 绘制箭头路径
        arrowPath.reset()
        arrowPath.moveTo(startX, startY)
        arrowPath.lineTo(endX, endY)
        arrowPath.moveTo(side1X, side1Y)
        arrowPath.lineTo(endX, endY)
        arrowPath.lineTo(side2X, side2Y)
    }
}