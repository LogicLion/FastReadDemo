package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.example.fastreaddemo.ext.dp
import java.lang.ref.WeakReference

/**
 * 字母/字符键盘view
 * @author wzh
 * @date 2023/8/14
 */
class CharacterKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    View(context, attrs) {

    private val alphaList = listOf(
        "Q", "W", "E", "R", "T", "Y",
        "U", "I", "O", "P", "A", "S",
        "D", "F", "G", "H", "J", "K", "L",
        "Z", "X", "C", "V", "B", "N", "M"
    )

    private val symbolList =
        listOf(
            "+", "-", "*", "÷", " ，", "。",
            " ;", "\"", "=", "/", "?", "<",
            ">", ":", "{", "}", "(", ")", "'",
            "\$", "%", "^", "@", "~", "#", "\$"
        )

    /**
     * true:字母，false:字符
     */
    var isAlphaStyle = true

    private var clickPosition: Int = -1

    private val mHandler: MyHandler = MyHandler(this)

    private class MyHandler(view: CharacterKeyboardView) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<CharacterKeyboardView> = WeakReference(view)

        override fun handleMessage(msg: Message) {
            val outerClass = weakReference.get()
            if (outerClass != null) {
                outerClass.clickPosition = -1
                outerClass.invalidate()
            }
        }
    }

    private val rectList = mutableListOf<RectF>()

    private var gridListener: OnGridListener? = null

    fun setGridListener(gridListener: OnGridListener) {
        this.gridListener = gridListener
    }

    private var cellWidth = 0f
    private var viewWidth = 0
    private var viewHeight = 0

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#707070")
        style = Paint.Style.STROKE
        strokeWidth = 1f.dp
    }

    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 13f.dp
        textAlign = Paint.Align.CENTER
    }

    private var bgPaint = Paint().apply {
        color = Color.parseColor("#1A1D38")
        style = Paint.Style.FILL
    }

    private var clickPaint = Paint().apply {
        color = Color.parseColor("#5CA2FF")
        style = Paint.Style.FILL
    }

    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener()).apply {
        setIsLongpressEnabled(false)
        setOnDoubleTapListener(null)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSize, widthSize * 3 / 10)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        cellWidth = w.toFloat() / 10

        createCellRect()
    }

    private fun createCellRect() {

        rectList.clear()
        for (position in 0..25) {
            val rectF = RectF()
            if (position < 10) {
                rectF.left = position * cellWidth
                rectF.top = 0f
                rectF.right = position * cellWidth + cellWidth
                rectF.bottom = cellWidth
            } else if (position in 10..18) {
                rectF.left = (position - 10) * cellWidth + cellWidth / 2
                rectF.top = cellWidth
                rectF.right = (position - 10) * cellWidth + cellWidth + cellWidth / 2
                rectF.bottom = cellWidth * 2
            } else {
                rectF.left = (position - 19) * cellWidth + cellWidth * 3 / 2
                rectF.top = cellWidth * 2
                rectF.right = (position - 19) * cellWidth + cellWidth + cellWidth * 3 / 2
                rectF.bottom = cellWidth * 3
            }
            rectList.add(rectF)
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val metricsInt = textPaint.fontMetricsInt

        for ((index, rect) in rectList.withIndex()) {
            canvas.drawRect(rect, bgPaint)
//            if (index == clickPosition) {
//                canvas.drawRect(rect, clickPaint)
//            }
            canvas.drawRect(rect, paint)

            val centerX = rect.centerX()
            val centerY =
                rect.centerY() - (metricsInt.descent + metricsInt.ascent) / 2

            if (isAlphaStyle) {
                canvas.drawText(alphaList[index], centerX, centerY, textPaint)
            } else {
                canvas.drawText(symbolList[index], centerX, centerY, textPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {

            var position: Int = -1
            val row = (e.y / cellWidth).toInt()
            if (row == 0) {
                val col = (e.x / cellWidth).toInt()
                position = col
            } else if (row == 1 && (e.x > cellWidth / 2 && e.x < viewWidth - cellWidth / 2)) {
                val col = ((e.x - cellWidth / 2) / cellWidth).toInt()
                position = col + 10
            } else if (row == 2 && (e.x > cellWidth * 3 / 2 && e.x < viewWidth - cellWidth * 3 / 2)) {
                val col = ((e.x - cellWidth * 3 / 2) / cellWidth).toInt()
                position = col + 19
            }

            if (position in alphaList.indices) {
                if (isAlphaStyle) {
                    gridListener?.onClick(alphaList[position])
                } else {
                    gridListener?.onClick(symbolList[position])
                }
                clickPosition = position
                invalidate()

                mHandler.removeCallbacksAndMessages(null)
                mHandler.sendEmptyMessageDelayed(position, 100)
            }

            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

    interface OnGridListener {
        fun onClick(itemClick: String)

    }
}