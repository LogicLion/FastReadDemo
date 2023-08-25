package com.example.fastreaddemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.View

/**
 * @author wzh
 * @date 2023/5/8
 */
abstract class CustomAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {


    //完整执行一次所需时长,毫秒
    protected var animationDuration = 3000L

    protected var repeat: Int = 1


    var isAnimationRunning: Boolean = false
        private set


    private var elapsedTime: Long = 0L

    //暂停时记录已消耗时长
    private var lastElapsedTime: Long = 0L


    private var animationStartTime: Long = 0L
    private var countDownTime = 0L

    var isPause = false
        private set


    var animatorListener: CustomAnimatorListener? = null


    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val frameTimeMillis = frameTimeNanos / 1_000_000

            if (isAnimationRunning) {
                updateAnimation(frameTimeMillis)

                Choreographer.getInstance().postFrameCallback(this)
            }
        }
    }


    fun startAnimation() {
        if (!isAnimationRunning) {
            isAnimationRunning = true
            Choreographer.getInstance().postFrameCallback(frameCallback)
            animatorListener?.onAnimationStart()

            if (isPause) {
                isPause = false
            } else {
                animatorListener?.onTimeCountDown(countDownTime.toInt())
            }
        }
    }


    fun pauseAnimation() {
        if (isAnimationRunning && !isPause) {
            //先停止绘制
            isAnimationRunning = false
            //移除帧绘制监听
            Choreographer.getInstance().removeFrameCallback(frameCallback)
            //记录已消耗时长
            lastElapsedTime = elapsedTime

            animationStartTime = 0

            invalidate()
            animatorListener?.onAnimationPause()
            isPause = true
        }
    }


    private fun stopAnimation() {
        Log.v("stopAnimation:", elapsedTime.toString())
        isAnimationRunning = false
        isPause = false
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        animationStartTime = 0
        elapsedTime = 0L
        lastElapsedTime = 0L
        countDownTime = 0
        onAnimationFinish()
        invalidate()

        animatorListener?.onAnimationEnd()
        animatorListener?.onTimeCountDown(animationDuration.toInt() * repeat / 1000)
    }

    protected open fun onAnimationFinish() {
    }

    fun setAnimationDuration(duration: Int) {
        if (!isAnimationRunning) {
            animationDuration = if (duration < 1) {
                1000L
            } else {
                duration * 1000L
            }
        }
    }

    fun setAnimationRepeat(animationRepeat: Int) {
        if (!isAnimationRunning) {
            repeat = if (animationRepeat < 1) {
                1
            } else {
                animationRepeat
            }
        }
    }

    private fun updateAnimation(currentTimeMillis: Long) {
        if (animationStartTime == 0L) {
            animationStartTime = currentTimeMillis
        }
        elapsedTime = currentTimeMillis - animationStartTime + lastElapsedTime

        if (elapsedTime < animationDuration * repeat) {

            val fraction = (elapsedTime % animationDuration).toFloat() / animationDuration
            onAnimationRunning(fraction)

            if (elapsedTime / 1000 != countDownTime) {
                countDownTime++
                animatorListener?.onTimeCountDown(countDownTime.toInt())
            }
        } else {
            onAnimationRunning(1f)
            stopAnimation()
        }
    }


    abstract fun onAnimationRunning(fraction: Float)


}