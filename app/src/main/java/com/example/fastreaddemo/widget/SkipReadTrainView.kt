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
import com.example.fastreaddemo.ext.toast
import java.lang.ref.WeakReference
import kotlin.math.pow
import kotlin.random.Random

/**
 * 跳读训练View
 * @author wzh
 * @date 2023/8/16
 */
class SkipReadTrainView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    /**
     * 行数
     */
    private var numRows = -1

    /**
     * 答题区列数
     */
    private var numCols = -1

    /**
     * 数字位数
     */
    private var digits = 1

    private var cellWidth = 0f
    private var cellHeight = 0f

    private var cellSpace = 0f

    private var cellRadius = 0f

    private var textSize = 12f.dp

    private val answerRectList = mutableListOf<RectF>()
    private val choiceOptionsRectList = mutableListOf<List<RectF>>()


    private var gridListener: OnGridListener? = null

    fun setGridListener(gridListener: OnGridListener) {
        this.gridListener = gridListener
    }


    private var clickErrorRow: Int = -1
    private var clickErrorCol: Int = -1

    private val mHandler: MyHandler = MyHandler(this)

    private class MyHandler(view: SkipReadTrainView) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<SkipReadTrainView> = WeakReference(view)

        override fun handleMessage(msg: Message) {
            val outerClass = weakReference.get()
            if (outerClass != null) {
                outerClass.clickErrorRow = -1
                outerClass.clickErrorCol = -1
                outerClass.invalidate()
            }
        }
    }


    //记录已完成的位置
    private val finishAnswerSet = HashSet<Int>()

    private val gestureDetector = GestureDetectorCompat(context, MyGestureListener()).apply {
        setIsLongpressEnabled(false)
        setOnDoubleTapListener(null)
    }

    /**
     * 答案区,最左的一列
     */
    private val answerTextList = mutableListOf<Int>()

    /**
     * 答题选项区
     */
    private val choiceOptionTextList = mutableListOf<List<Int>>()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 0.3f.dp
    }

    private var defaultPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A1D38")
        style = Paint.Style.FILL
    }

    private var correctPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5CA2FF")
        style = Paint.Style.FILL
    }

    private var wrongPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF5454")
        style = Paint.Style.FILL
    }

    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 24f.dp
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (numCols == -1 && numRows == -1) {
            return
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)


        if (widthMode == MeasureSpec.EXACTLY) {
            cellSpace = if (numCols <= 3) 20f.dp else 8f.dp

            val cellW = (widthSize - (numCols + 1) * cellSpace) / (numCols + 1)
            val cellH = cellW * 3 / 4
            cellRadius = (5 + 7 * (8 - 4.coerceAtLeast(numCols.coerceAtMost(8))) / 4).toFloat().dp

            setMeasuredDimension(widthSize, (cellH * numRows + cellSpace * (numRows - 1)).toInt())
        } else if (heightMode == MeasureSpec.EXACTLY) {
            cellSpace = 5f.dp
            var cellW = 68f.dp
            var cellH = cellW /2

            if ((cellH * numRows + cellSpace * (numRows - 1)) > heightSize) {
                cellH = (heightSize - cellSpace * (numRows - 1)) / numRows
                cellW = cellH *2
            }

            cellRadius = 5f.dp

            val specWidth = ((cellW + cellSpace) * numRows).toInt()
            setMeasuredDimension(specWidth, (cellH * numRows + cellSpace * (numRows - 1)).toInt())
        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (numCols == -1 && numRows == -1) {
            return
        }

        cellWidth = (w - cellSpace * (numCols + 1)) / (numCols + 1)
        cellHeight = (h - cellSpace * (numRows - 1)) / numRows

        val tempStr = generateRandomNumber(digits)
        textPaint.textSize = adapterTextSizeForCellWidth(tempStr.toString(), textSize)

        createCellRect()
    }

    private val tempPaint by lazy {
        Paint(textPaint)
    }

    private fun adapterTextSizeForCellWidth(tempMeasureText: String, textSize: Float): Float {
        tempPaint.textSize = textSize
        return if (tempPaint.measureText(tempMeasureText) > cellWidth - 3f.dp) {
            adapterTextSizeForCellWidth(tempMeasureText, textSize - 1f.dp)
        } else {
            textSize
        }
    }

    private fun createCellRect() {
        answerRectList.clear()
        choiceOptionsRectList.clear()

        for (i in 0 until numRows) {

            val rowList = mutableListOf<RectF>()
            for (j in 0 until numCols) {
                val rectF = RectF()
                val margeLeft = cellWidth + cellSpace * 2
                rectF.left = j * (cellWidth + cellSpace) + margeLeft
                rectF.right = j * (cellWidth + cellSpace) + margeLeft + cellWidth
                rectF.top = i * (cellHeight + cellSpace)
                rectF.bottom = i * (cellHeight + cellSpace) + cellHeight

                rowList.add(rectF)
            }
            choiceOptionsRectList.add(rowList)
        }

        for (k in 0 until numRows) {
            val rectF = RectF()
            rectF.left = 0f
            rectF.right = cellWidth
            rectF.top = k * (cellHeight + cellSpace)
            rectF.bottom = k * (cellHeight + cellSpace) + cellHeight
            answerRectList.add(rectF)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (numCols == -1 && numRows == -1) {
            return
        }

        val metricsInt = textPaint.fontMetricsInt

        for ((index, rectF) in answerRectList.withIndex()) {
            canvas.drawRoundRect(rectF, cellRadius, cellRadius, correctPaint)

            val centerX = rectF.centerX()
            val centerY =
                rectF.centerY() - (metricsInt.descent + metricsInt.ascent) / 2

            canvas.drawText(answerTextList[index].toString(), centerX, centerY, textPaint)
        }

        for ((row, rowList) in choiceOptionsRectList.withIndex()) {

            for ((col, rectF) in rowList.withIndex()) {
                val optionText = choiceOptionTextList[row][col]

                if (optionText == answerTextList[row] && finishAnswerSet.contains(row)) {
                    canvas.drawRoundRect(rectF, cellRadius, cellRadius, correctPaint)
                } else {

                    if (clickErrorRow == row && clickErrorCol == col) {
                        canvas.drawRoundRect(rectF, cellRadius, cellRadius, wrongPaint)
                    } else {
                        canvas.drawRoundRect(rectF, cellRadius, cellRadius, defaultPaint)
                    }
                }

                canvas.drawRoundRect(
                    rectF.left + 0.5f.dp,
                    rectF.top + 0.5f.dp,
                    rectF.right - 0.5f.dp,
                    rectF.bottom - 0.5f.dp,
                    cellRadius,
                    cellRadius,
                    paint
                )

                val centerX = rectF.centerX()
                val centerY =
                    rectF.centerY() - (metricsInt.descent + metricsInt.ascent) / 2

                canvas.drawText(optionText.toString(), centerX, centerY, textPaint)
            }
        }

        canvas.drawLine(cellWidth + cellSpace, 0f, cellWidth + cellSpace, height.toFloat(), paint)

    }

    /**
     * @param digits 数字位数
     * @param rows 指定行数
     * @param cols 指定列数
     */
    fun setCellNum(digits: Int, rows: Int, cols: Int) {
        if (digits in 1..7 && rows > 0 && cols > 0) {
            numRows = rows
            numCols = cols
            this.digits = digits

            answerTextList.clear()
            answerTextList.addAll(generateRandomNumberList(digits, numRows, -1))

            choiceOptionTextList.clear()
            for (i in 0 until numRows) {
                val answerRowList = mutableListOf<Int>()
                answerRowList.addAll(generateRandomNumberList(digits, numCols, answerTextList[i]))
                answerRowList.shuffle()
                choiceOptionTextList.add(answerRowList)
            }

            requestLayout()
        } else {
            "参数错误".toast()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (numRows == -1 || numCols == -1) {
            false
        } else {
            gestureDetector.onTouchEvent(event)
        }
    }

    /**
     * 生成[count]个不重复的数字位数为[digits]的数字,其中包含[referNumber]（不为-1时）,其他生成的数字不与[referNumber]相同,
     */
    private fun generateRandomNumberList(digits: Int, count: Int, referNumber: Int): List<Int> {

        val generatedNumbers = mutableSetOf<Int>()

        if (referNumber != -1) {
            generatedNumbers.add(referNumber)
        }
        while (generatedNumbers.size < count) {
            val randomNumber = generateRandomNumber(digits)
            if (randomNumber != referNumber) {
                generatedNumbers.add(randomNumber)
            }
        }

        return generatedNumbers.toList()
    }

    private fun generateRandomNumber(digits: Int): Int {
        val min = 10.0.pow(digits - 1).toInt()
        val max = 10.0.pow(digits).toInt() - 1

        return Random.nextInt(min, max)

    }


    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {

            var row: Int = -1
            var col: Int = -1
            if (e.y < cellHeight) {
                row = 0
            } else {
                row = ((e.y - cellHeight) / (cellHeight + cellSpace) + 1).toInt()
            }

            val leftMargin = cellWidth + cellSpace * 2
            val startX = e.x - leftMargin
            if (startX > 0) {
                col = if (startX <= cellWidth) {
                    0
                } else {
                    ((startX - cellWidth) / (cellWidth + cellSpace) + 1).toInt()
                }
            }

            if ((row in 0 until numRows) && (col in 0 until numCols)) {
                if (choiceOptionTextList[row][col] == answerTextList[row]) {
                    if (!finishAnswerSet.contains(row)) {
                        //答对
                        finishAnswerSet.add(row)
                        invalidate()

                        if (finishAnswerSet.size == numRows) {
                            gridListener?.onFinish()
                        }
                    }
                } else {
                    //答错
                    clickErrorRow = row
                    clickErrorCol = col
                    invalidate()

                    mHandler.removeCallbacksAndMessages(null)
                    mHandler.sendEmptyMessageDelayed(clickErrorRow, 100)
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

        fun onFinish()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mHandler.removeCallbacksAndMessages(null)
    }


}