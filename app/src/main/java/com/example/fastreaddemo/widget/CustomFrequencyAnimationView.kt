package com.example.fastreaddemo.widget

import android.content.Context
import android.util.AttributeSet

/**
 * 指定频次执行的动画
 * @author wzh
 * @date 2023/8/4
 */
abstract class CustomFrequencyAnimationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : CustomAnimationView(context, attrs) {

    private var currProgress: Int = -1

    final override fun onAnimationRunning(fraction: Float) {
        val progress =
            (fraction * (getFrequencySize() + 1) - 1).toInt()

        if ((progress in 0 until getFrequencySize()) && progress != currProgress) {
            currProgress = progress
            onFrequencyUpdate(progress)
        }
    }

    override fun onAnimationFinish() {
        super.onAnimationFinish()
        currProgress = -1
    }

    abstract fun onFrequencyUpdate(progress: Int)

    abstract fun getFrequencySize(): Int
}