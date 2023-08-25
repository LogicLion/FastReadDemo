package com.example.fastreaddemo.widget

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import com.example.fastreaddemo.ext.dp
import com.example.fastreaddemo.ext.toast
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

/**
 * @author wzh
 * @date 2023/8/18
 */
class SkipReadTrainLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ViewGroup(context, attrs) {

    /**
     * 答案区,最左的一列
     */
    private val answerTextList = mutableListOf<Int>()


    /**
     * 答题选项区
     */
    private val choiceOptionTextList = mutableListOf<List<Int>>()

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

    private var cellWidth = 60.dp
    private var cellHeight = 30.dp

    private var cellSpace = 5.dp

    private var cellRadius = 0f

    private var textSize = 12f.dp

    private val answerTextViewList = mutableListOf<TextView>()
    private val choiceOptionsTextViewList = mutableListOf<List<TextView>>()


    //记录已完成的位置
    private val finishAnswerSet = HashSet<Int>()

    private val TAG = "SkipReadTrainLayout"

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 0.3f.dp
    }

    private val defaultDrawable = GradientDrawable().apply {

        // 设置形状为矩形
        shape = GradientDrawable.RECTANGLE
        // 设置背景颜色
//        gradientDrawable.setColor(Color.WHITE)
        // 设置圆角半径，这里设置为 5dp
        cornerRadius = 5f.dp

        // 设置描边宽度和颜色
        val strokeWidth = max(1, (0.5f.dp).toInt())
        setStroke(strokeWidth, Color.WHITE)

    }

    private val correctDrawable = GradientDrawable().apply {
        // 设置形状为矩形
        shape = GradientDrawable.RECTANGLE
        // 设置背景颜色
        setColor(Color.parseColor("#5CA2FF"))
        // 设置圆角半径，这里设置为 5dp
        cornerRadius = 5f.dp

        // 设置描边宽度和颜色
//        val strokeWidth = max(1, (0.5f.dp).toInt())
//        setStroke(strokeWidth, Color.WHITE)
    }


    private val wrongDrawable = GradientDrawable().apply {
        // 设置形状为矩形
        shape = GradientDrawable.RECTANGLE
        // 设置背景颜色
        setColor(Color.parseColor("#FF5454"))
        // 设置圆角半径，这里设置为 5dp
        cornerRadius = 5f.dp

        // 设置描边宽度和颜色
//        val strokeWidth = max(1, (0.5f.dp).toInt())
//        setStroke(strokeWidth, Color.WHITE)
    }


    init {
        setWillNotDraw(false)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.v(TAG, "onMeasure")

        if (numCols == -1 && numRows == -1) {
            return
        }

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)


        if (widthMode == MeasureSpec.EXACTLY) {
            cellSpace = if (numCols <= 3) 20.dp else 8.dp

            val cellW = (widthSize - (numCols + 1) * cellSpace) / (numCols + 1)
            val cellH = cellW * 3 / 4
            cellRadius = (5 + 7 * (8 - 4.coerceAtLeast(numCols.coerceAtMost(8))) / 4).toFloat().dp

            // 测量子view的宽高，让每个格子占满整个布局
            val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                cellW,
                MeasureSpec.EXACTLY
            )
            val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                cellH,
                MeasureSpec.EXACTLY
            )
//
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }

            setMeasuredDimension(widthSize, (cellH * numRows + cellSpace * (numRows - 1)))
        } else if (heightMode == MeasureSpec.EXACTLY) {
            cellSpace = 5.dp
            var cellW = 68.dp
            var cellH = cellW / 2

            if ((cellH * numRows + cellSpace * (numRows - 1)) > heightSize) {
                cellH = ((heightSize - cellSpace * (numRows - 1)) / numRows)
                cellW = cellH * 2
            }

            cellRadius = 5f.dp

            val specWidth = ((cellW + cellSpace) * numRows).toInt()
            setMeasuredDimension(specWidth, (cellH * numRows + cellSpace * (numRows - 1)))
        }


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            (cellWidth + cellSpace).toFloat(),
            0f,
            (cellWidth + cellSpace).toFloat(),
            height.toFloat(),
            paint
        )
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Log.v(TAG, "onSizeChanged")

        if (numCols == -1 && numRows == -1) {
            return
        }

        cellWidth = ((w - cellSpace * (numCols + 1)) / (numCols + 1))
        cellHeight = ((h - cellSpace * (numRows - 1)) / numRows)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        Log.v(TAG, "onLayout")

        if (numCols == -1 && numRows == -1) {
            return
        }

        for ((index, textView) in answerTextViewList.withIndex()) {
            textView.layout(
                0,
                (cellSpace + cellHeight) * index,
                cellWidth,
                (cellSpace + cellHeight) * index + cellHeight
            )
        }


        for ((i, rowList) in choiceOptionsTextViewList.withIndex()) {
            for ((j, textView) in rowList.withIndex()) {
                val margeLeft = cellWidth + cellSpace * 2
                textView.layout(
                    j * (cellWidth + cellSpace) + margeLeft,
                    i * (cellHeight + cellSpace),
                    j * (cellWidth + cellSpace) + margeLeft + cellWidth,
                    i * (cellHeight + cellSpace) + cellHeight
                )
            }
        }

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

            createChildView()
            requestLayout()
        } else {
            "参数错误".toast()
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

    private fun createChildView() {
        answerTextViewList.clear()
        choiceOptionsTextViewList.clear()
        removeAllViews()

        for (i in 0 until numRows) {

            val rowList = mutableListOf<TextView>()
            for (j in 0 until numCols) {
                val tv = createTextView()
                tv.text = choiceOptionTextList[i][j].toString()
                tv.background = defaultDrawable

                if (choiceOptionTextList[i][j] == answerTextList[i]) {
                    tv.setOnClickListener {
                        if (!finishAnswerSet.contains(i)) {
                            finishAnswerSet.add(i)
                            tv.background = correctDrawable
                        }
                    }
                } else {

                    val wrongStateDrawable = StateListDrawable().apply {
                        addState(intArrayOf(R.attr.state_pressed), wrongDrawable)
                        addState(intArrayOf(), defaultDrawable)
                    }
                    tv.background = wrongStateDrawable
                }
                rowList.add(tv)
                addView(tv)
            }
            choiceOptionsTextViewList.add(rowList)
        }

        for (k in 0 until numRows) {
            val tv = createTextView()
            tv.text = answerTextList[k].toString()
            tv.background = correctDrawable
            answerTextViewList.add(tv)
            addView(tv)
        }
    }

    private fun createTextView(): TextView {
        val tv = TextView(context)
        tv.text = "测试"
        tv.setTextColor(Color.WHITE)
        tv.gravity = Gravity.CENTER
        tv.isClickable = true
        tv.setLines(1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv.setAutoSizeTextTypeUniformWithConfiguration(1,200,1, TypedValue.COMPLEX_UNIT_DIP)
        }
        return tv
    }
}