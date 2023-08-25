package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastreaddemo.R
import com.example.fastreaddemo.adapter.SentenceOptionAdapter
import com.example.fastreaddemo.data.FastReadOption
import com.example.fastreaddemo.databinding.FastreadFragmentSentenceFlashBinding
import com.example.fastreaddemo.ext.toast
import java.lang.ref.WeakReference
import kotlin.random.Random

/**
 * 句子闪现
 * @author wzh
 * @date 2023/8/18
 */
class SentenceFlashFragment : BaseFastReadFragment<FastreadFragmentSentenceFlashBinding>() {

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

    private var currAnswerStatus = NumberFlashFragment.AnswerStatus.IN_ROUND


    private var correctPosition: Int = 0
    private var correctSentence: String? = null

    private var optionList = mutableListOf<FastReadOption>()

    private val adapter: SentenceOptionAdapter = SentenceOptionAdapter()

    override fun setupGameLayoutId() = R.layout.fastread_fragment_sentence_flash


    private val mHandler: MyHandler = MyHandler(this)


    private class MyHandler(fragment: SentenceFlashFragment) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<SentenceFlashFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.setFlashTextShow(false)
        }
    }

    override fun initGameView() {

        optionList.add(FastReadOption("七个个个个1字", null, 0))
        optionList.add(FastReadOption("七个个个个2字", null, 0))
        optionList.add(FastReadOption("七个个个个3字", null, 0))
        optionList.add(FastReadOption("七个个个个4字", null, 0))
        setupCorrectSentence(optionList)
        adapter.setList(optionList)
        mGameBinding.rvOption.layoutManager = object : LinearLayoutManager(requireActivity()) {
            override fun canScrollVertically() = false
        }
        mGameBinding.rvOption.adapter = adapter

        adapter.setOnItemClickListener { adapter, view, position ->

            if (currAnswerStatus == NumberFlashFragment.AnswerStatus.IN_ROUND) {
                currAnswerStatus = NumberFlashFragment.AnswerStatus.IN_ROUND
                val data = adapter.data[position]
                if (position == correctPosition) {
                    (data as FastReadOption).status = 1
                } else {
                    (data as FastReadOption).status = 2
                }
                adapter.notifyDataSetChanged()

                mGameBinding.tvNext.visibility = View.VISIBLE
                if (round >= (roundNum - 1)) {
                    currAnswerStatus = NumberFlashFragment.AnswerStatus.ANSWER_COMPLETE
                    mGameBinding.tvNext.text = "完成答题"
                } else {
                    currAnswerStatus = NumberFlashFragment.AnswerStatus.ROUND_COMPLETE
                    mGameBinding.tvNext.text = "下一题"
                }
            } else {
                "该题已作答完成".toast()
            }

        }

        mGameBinding.tvNext.setOnClickListener {
            if (currAnswerStatus == NumberFlashFragment.AnswerStatus.ANSWER_COMPLETE) {
                "提交".toast()
            } else {
                currAnswerStatus = NumberFlashFragment.AnswerStatus.IN_ROUND

                optionList.forEach {
                    it.status = 0
                }
                adapter.setList(optionList)

                setupCorrectSentence(optionList)
                mGameBinding.tvNext.visibility = View.INVISIBLE

                round++
                setFlashTextShow(true)
            }
        }

        mGameBinding.tvAgain.postDelayed({
            setFlashTextShow(true)
        }, 500)

        mGameBinding.tvAgain.setOnClickListener {
            setFlashTextShow(true)
        }

    }


    private fun setFlashTextShow(isShow: Boolean) {
        if (isShow) {
            mGameBinding.tvAgain.visibility = View.INVISIBLE
            mGameBinding.tvTargetText.text = correctSentence
            mGameBinding.tvTargetText.visibility = View.VISIBLE
            mHandler.sendEmptyMessageDelayed(999, flashTime)
        } else {
            mGameBinding.tvTargetText.visibility = View.INVISIBLE
            mGameBinding.tvAgain.visibility = View.VISIBLE
        }
    }


    private fun setupCorrectSentence(list: List<FastReadOption>) {
        correctPosition = Random.nextInt(0, list.size)
        correctSentence = list[correctPosition].title
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