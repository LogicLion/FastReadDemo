package com.example.fastreaddemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.example.fastreaddemo.ext.dp

/**
 * 视点止移
 * @author wzh
 * @date 2023/8/1
 */
class RectFlashMoveView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    CustomFrequencyAnimationView(context, attrs) {
    private val rectWidthSeed = mutableListOf<Int>()
    private val space = 5.dp
    var lines = 6
    private var rectHeight = 28.dp

    private var currRect: Rect? = null

    private val rectList = mutableListOf<Rect>()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#616161")
        style = Paint.Style.FILL
    }


    private val focusPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5CA2FF")
        style = Paint.Style.FILL
    }


    override fun onAnimationFinish() {
        super.onAnimationFinish()
        currRect = null
    }

    override fun onFrequencyUpdate(progress: Int) {
        currRect = rectList[progress]
        invalidate()
    }

    override fun getFrequencySize() = rectList.size


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        generateSeedWidth(w, h)
        computeRectWidth(w, h)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rectList.forEach {
            canvas.drawRect(it, paint)
        }

        currRect?.let {
            canvas.drawRect(it, focusPaint)
        }

    }

    private fun generateSeedWidth(w: Int, h: Int) {
        val usableWidth: Int
        val allSeed: Int
        if (h > w) {
            usableWidth = w - space * 3
            allSeed = 155 + 95 + 205 + 255
        } else {
            usableWidth = w - space * 7
            allSeed = (155 + 95 + 205 + 255) * 2
        }

        rectWidthSeed.add(155 * usableWidth / allSeed)
        rectWidthSeed.add(95 * usableWidth / allSeed)
        rectWidthSeed.add(205 * usableWidth / allSeed)
        rectWidthSeed.add(255 * usableWidth / allSeed)
    }

    private fun getRandomElement(): Int {
        val randomIndex = kotlin.random.Random.nextInt(rectWidthSeed.size)
        return rectWidthSeed[randomIndex]
    }

    private fun computeRectWidth(w: Int, h: Int) {
        var usableWidth = w

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0
        var currLine = 1

        while (currLine <= lines) {
            val randomWidth = getRandomElement()
            if (randomWidth < usableWidth - 50.dp) {
                left = right + space
                right = left + randomWidth
                top = (currLine - 1) * (rectHeight + space)
                bottom = top + rectHeight

                val rect = Rect(left, top, right, bottom)
                rectList.add(rect)

                usableWidth = usableWidth - 5.dp - randomWidth
            } else {
                left = right + space
                right = w
                top = (currLine - 1) * (rectHeight + space)
                bottom = top + rectHeight
                val rect = Rect(left, top, right, bottom)
                rectList.add(rect)
                usableWidth = w

                left = 0
                right = 0
                currLine++
            }


        }
    }

}