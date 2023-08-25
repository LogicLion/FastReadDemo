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
 * 数字键盘
 * @author wzh
 * @date 2023/8/14
 */
class NumericKeypadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val numberList = listOf(
        "1", "2", "3", "4", "5", "6",
        "7", "8", "9", "0", "确 定"
    )


    private var clickPosition: Int = -1

    private val mHandler: MyHandler = MyHandler(this)

    private class MyHandler(view: NumericKeypadView) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<NumericKeypadView> = WeakReference(view)

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
        textSize = 24f.dp
        textAlign = Paint.Align.CENTER
    }

    private var clickPaint = Paint().apply {
        color = Color.parseColor("#FFC339")
        style = Paint.Style.FILL
    }

    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener()).apply {
        setIsLongpressEnabled(false)
        setOnDoubleTapListener(null)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(widthSize, widthSize * 4 / 3)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        cellWidth = w.toFloat() / 3

        createCellRect()
    }

    private fun createCellRect() {

        rectList.clear()
        for (position in 0..10) {
            val row = position / 3
            val col = position % 3
            val rectF = RectF()

            if (row == 3 && col != 0) {
                rectF.right = col * cellWidth + cellWidth * 2
            } else {
                rectF.right = col * cellWidth + cellWidth
            }

            rectF.top = row * cellWidth
            rectF.left = col * cellWidth
            rectF.bottom = row * cellWidth + cellWidth
            rectList.add(rectF)
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val metricsInt = textPaint.fontMetricsInt

        for ((index, rect) in rectList.withIndex()) {
            if (index == clickPosition) {
                canvas.drawRect(rect, clickPaint)
            }
            canvas.drawRect(rect, paint)

            val centerX = rect.centerX()
            val centerY =
                rect.centerY() - (metricsInt.descent + metricsInt.ascent) / 2

            canvas.drawText(numberList[index], centerX, centerY, textPaint)

        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {

            val position: Int
            val row = (e.y / cellWidth).toInt()
            val col = (e.x / cellWidth).toInt()
            position = if (row == 3) {
                if (col == 0) {
                    9
                } else {
                    10
                }
            } else {
                row * 3 + col
            }

            if (position < numberList.size) {
                if (position == 10) {
                    gridListener?.onConfirm()

                    clickPosition = position
                    invalidate()

                    mHandler.removeCallbacksAndMessages(null)
                    mHandler.sendEmptyMessageDelayed(position, 100)
                }else{
                    gridListener?.onClick(numberList[position])
                }

            }

            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

    interface OnGridListener {
        fun onClick(itemClick: String)

        fun onConfirm()

    }
}