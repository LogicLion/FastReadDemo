package com.example.fastreaddemo.widget
/**
 * @author wzh
 * @date 2023/5/12
 */
interface CustomAnimatorListener {

    fun onAnimationStart()
    fun onAnimationPause()
    fun onAnimationResume()
    fun onAnimationEnd()
    fun onTimeCountDown(second: Int)
}