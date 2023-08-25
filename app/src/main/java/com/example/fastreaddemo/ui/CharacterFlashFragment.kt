package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastReadType
import com.example.fastreaddemo.databinding.FastreadFragmentCharacterFlashBinding
import com.example.fastreaddemo.ext.toast
import com.example.fastreaddemo.widget.CharacterKeyboardView
import com.qunyu.dividedroad.mvvm.ext.buildSpannableString
import java.lang.ref.WeakReference

/**
 * @author wzh
 * @date 2023/8/14
 */
class CharacterFlashFragment : BaseFastReadFragment<FastreadFragmentCharacterFlashBinding>() {
    override fun setupGameLayoutId() = R.layout.fastread_fragment_character_flash


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


    private val shuffledList = mutableListOf<String>()


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

    private var currAnswerStatus = AnswerStatus.IN_ROUND


    private val mHandler: MyHandler = MyHandler(this)

    private class MyHandler(fragment: CharacterFlashFragment) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<CharacterFlashFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.setFlashTextShow(false)
        }
    }


    override fun initGameView() {

        shuffledList.clear()
        shuffledList.addAll(generateRandomCharacterList(digits, roundNum))

        mGameBinding.characterView.isAlphaStyle = gameConfig.gameType != FastReadType.SYMBOL_FLASH


        mGameBinding.characterView.setGridListener(object : CharacterKeyboardView.OnGridListener {
            override fun onClick(itemClick: String) {
                if (currAnswerStatus == AnswerStatus.IN_ROUND) {
                    mGameBinding.tvInputText.append(itemClick)
                } else {
                    "该题已作答完成".toast()
                }
            }


        })

        mGameBinding.tvConfirm.setOnClickListener {
            if (currAnswerStatus == AnswerStatus.IN_ROUND) {

                val inputText = mGameBinding.tvInputText.text.toString()
                if (inputText.isEmpty()) {
                    "请输入闪现内容".toast()
                    return@setOnClickListener
                }
                mGameBinding.tvNext.visibility = View.VISIBLE

                compareText(
                    inputText,
                    shuffledList[round]
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

    private fun generateRandomCharacterList(digits: Int, count: Int): List<String> {

        val generatedNumbers = mutableSetOf<String>()

        while (generatedNumbers.size < count) {
            val randomNumber = generateRandomCharacter(digits)
            generatedNumbers.add(randomNumber)
        }

        return generatedNumbers.toList()
    }


    private fun generateRandomCharacter(digits: Int): String {
        val list = mutableListOf<String>()

        if (gameConfig.gameType == FastReadType.SYMBOL_FLASH) {
            list.addAll(symbolList)
        } else {
            list.addAll(alphaList)
        }

        list.shuffle()
        val subList: List<String> = list.subList(0, digits)

        return subList.joinToString("")

    }

    private fun setFlashTextShow(isShow: Boolean) {
        if (isShow) {
            mGameBinding.tvAgain.visibility = View.INVISIBLE
            mGameBinding.tvTargetText.text = shuffledList[round]
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