package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentNumberFlashBinding
import com.example.fastreaddemo.ext.toast
import com.example.fastreaddemo.widget.NumericKeypadView
import com.qunyu.dividedroad.mvvm.ext.buildSpannableString
import java.lang.ref.WeakReference
import kotlin.math.pow
import kotlin.random.Random

/**
 * @author wzh
 * @date 2023/8/15
 */
class NumberFlashFragment : BaseFastReadFragment<FastreadFragmentNumberFlashBinding>() {

    /**
     * 字符位数
     */
    private var digits = 3


    /**
     * 总回合数
     */
    private var roundNum = 3


    /**
     * 当前回合数
     */
    private var round = 0


    /**
     * 闪现时间
     */
    private var flashTime = 200L


    private val numberList = mutableListOf<Int>()

    private var currAnswerStatus = AnswerStatus.IN_ROUND


    private val mHandler: MyHandler = MyHandler(this)

    private class MyHandler(fragment: NumberFlashFragment) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<NumberFlashFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.setFlashTextShow(false)
        }
    }


    override fun setupGameLayoutId() = R.layout.fastread_fragment_number_flash

    override fun initGameView() {

        numberList.clear()
        numberList.addAll(generateRandomNumberList(digits, roundNum))

        mGameBinding.numericKeypadView.setGridListener(object : NumericKeypadView.OnGridListener {
            override fun onClick(itemClick: String) {
                if (currAnswerStatus == AnswerStatus.IN_ROUND) {
                    mGameBinding.tvInputText.append(itemClick)
                } else {
                    "该题已作答完成".toast()
                }
            }

            override fun onConfirm() {
                if (currAnswerStatus == AnswerStatus.IN_ROUND) {

                    val inputText = mGameBinding.tvInputText.text.toString()
                    if (inputText.isEmpty()) {
                        "请输入闪现内容".toast()
                        return
                    }

                    mGameBinding.tvNext.visibility = View.VISIBLE
                    compareText(
                        inputText,
                        numberList[round].toString()
                    )
                    if (round >= (roundNum - 1)) {
                        currAnswerStatus = AnswerStatus.ANSWER_COMPLETE
                        mGameBinding.tvNext.text = "完成答题"
                    } else {
                        currAnswerStatus = AnswerStatus.ROUND_COMPLETE
                        mGameBinding.tvNext.text = "下一题"
                    }
                } else {
                    "该题已作答完成".toast()
                }
            }

        })

        mGameBinding.tvNext.setOnClickListener {
            if (currAnswerStatus == AnswerStatus.ANSWER_COMPLETE) {
                "提交".toast()
            } else {
                currAnswerStatus = AnswerStatus.IN_ROUND
                mGameBinding.tvInputText.text = ""
                mGameBinding.tvNext.visibility = View.INVISIBLE

                round++
                setFlashTextShow(true)
            }


        }

        mGameBinding.tvAgain.setOnClickListener {
            setFlashTextShow(true)
        }

        mGameBinding.tvAgain.postDelayed({
            setFlashTextShow(true)
        }, 500)

        mGameBinding.ivClearInput.setOnClickListener {
            mGameBinding.tvInputText.text = ""
        }

        mGameBinding.tvInputText.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mGameBinding.ivClearInput.visibility = View.INVISIBLE
            } else {
                mGameBinding.ivClearInput.visibility = View.VISIBLE
            }
        }

    }

    private fun compareText(inputText: String, correctText: String) {

        mGameBinding.tvInputText.buildSpannableString {
            for ((index, text) in inputText.withIndex()) {
                if (index < correctText.length) {
                    if (text == correctText[index]) {
                        addText(text.toString())
                    } else {
                        addText(text.toString()) { setColor("#FF5454") }
                    }
                } else {
                    addText(text.toString()) { setColor("#FF5454") }
                }

            }
        }
    }

    private fun generateRandomNumberList(digits: Int, count: Int): List<Int> {

        val generatedNumbers = mutableSetOf<Int>()

        while (generatedNumbers.size < count) {
            val randomNumber = generateRandomNumber(digits)
            generatedNumbers.add(randomNumber)
        }

        return generatedNumbers.toList()
    }


    private fun generateRandomNumber(digits: Int): Int {
        val min = 10.0.pow(digits - 1).toInt()
        val max = 10.0.pow(digits).toInt() - 1

        return Random.nextInt(min, max)

    }

    private fun setFlashTextShow(isShow: Boolean) {
        if (isShow) {
            mGameBinding.tvAgain.visibility = View.INVISIBLE
            mGameBinding.tvTargetText.text = numberList[round].toString()
            mGameBinding.tvTargetText.visibility = View.VISIBLE
            mHandler.sendEmptyMessageDelayed(999, flashTime)
        } else {
            mGameBinding.tvTargetText.visibility = View.INVISIBLE
            mGameBinding.tvAgain.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacksAndMessages(null)
    }

    enum class AnswerStatus {
        /**
         * 回合进行中
         */
        IN_ROUND,

        /**
         * 当前回合完成
         */
        ROUND_COMPLETE,

        /**
         * 作答完成
         */
        ANSWER_COMPLETE
    }
}