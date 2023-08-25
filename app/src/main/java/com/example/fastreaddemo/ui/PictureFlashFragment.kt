package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastreaddemo.R
import com.example.fastreaddemo.adapter.PictureFlashAdapter
import com.example.fastreaddemo.adapter.PictureOptionAdapter
import com.example.fastreaddemo.data.FastFlashItem
import com.example.fastreaddemo.data.FastReadOption
import com.example.fastreaddemo.databinding.FastreadFragmentPictureFlashBinding
import com.example.fastreaddemo.ext.toast
import java.lang.ref.WeakReference
import kotlin.random.Random

/**
 * 图片闪现
 * @author wzh
 * @date 2023/8/18
 */
class PictureFlashFragment : BaseFastReadFragment<FastreadFragmentPictureFlashBinding>() {

    /**
     * 总回合数
     */
    private var roundNum = 1


    /**
     * 当前回合数
     */
    private var round = 0


    /**
     * 闪现时间
     */
    private var flashTime = 200L

    private var currAnswerStatus = AnswerStatus.IN_ROUND


    private var correctPosition: Int = 0
    private var flashPosition: Int = 0
    private var correctImg: String? = null
    private val flashAdapter = PictureFlashAdapter()
    private val optionAdapter = PictureOptionAdapter()


    private var optionList = mutableListOf<FastReadOption>()
    private var flashItemList = mutableListOf<FastFlashItem>()


    override fun setupGameLayoutId() = R.layout.fastread_fragment_picture_flash


    private val mHandler: MyHandler = MyHandler(this)


    private class MyHandler(fragment: PictureFlashFragment) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<PictureFlashFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.setFlashShow(false)
        }
    }

    override fun initGameView() {

        optionList.add(
            FastReadOption(
                "七个个个个1字",
                "https://img0.baidu.com/it/u=2769111648,1830853668&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=537",
                0
            )
        )
        optionList.add(
            FastReadOption(
                "七个个个个2字",
                "https://p5.itc.cn/images01/20220501/5058ededd47d4757b142659aebd5ce60.jpeg",
                0
            )
        )
        optionList.add(
            FastReadOption(
                "七个个个个3字",
                "https://img1.baidu.com/it/u=567957465,3098180154&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=914",
                0
            )
        )
        optionList.add(
            FastReadOption(
                "七个个个个4字",
                "https://p1.itc.cn/images01/20220622/64150be722444ed5a19a1efc7dc2f8e1.jpeg",
                0
            )
        )
        setupFlashImg(optionList)
        optionAdapter.setList(optionList)
        mGameBinding.rvOption.layoutManager = object : LinearLayoutManager(
            requireActivity(),
            HORIZONTAL, false
        ) {
            override fun canScrollVertically() = false
        }
        mGameBinding.rvOption.adapter = optionAdapter

        optionAdapter.setOnItemClickListener { adapter, view, position ->

            if (currAnswerStatus == AnswerStatus.IN_ROUND) {
                currAnswerStatus = AnswerStatus.IN_ROUND
                val data = adapter.data[position]
                if (position == correctPosition) {
                    (data as FastReadOption).status = 1
                } else {
                    (data as FastReadOption).status = 2
                }
                adapter.notifyDataSetChanged()

                mGameBinding.tvNext.visibility = View.VISIBLE
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

        mGameBinding.rvTargetImg.layoutManager = GridLayoutManager(requireActivity(), 3)
        mGameBinding.rvTargetImg.adapter = flashAdapter

        mGameBinding.tvNext.setOnClickListener {
            if (currAnswerStatus == AnswerStatus.ANSWER_COMPLETE) {
                "提交".toast()
            } else {
                currAnswerStatus = AnswerStatus.IN_ROUND
                setupFlashImg(optionList)
                mGameBinding.tvNext.visibility = View.INVISIBLE

                round++
                setFlashShow(true)
            }
        }

        mGameBinding.tvAgain.postDelayed({
            setFlashShow(true)
        }, 500)

        mGameBinding.tvAgain.setOnClickListener {
            setFlashShow(true)
        }

    }


    private fun setFlashShow(isShow: Boolean) {
        if (isShow) {
            flashAdapter.data[flashPosition].isShow = true
            flashAdapter.notifyItemChanged(flashPosition)
            mHandler.sendEmptyMessageDelayed(999, flashTime)
        } else {
            flashAdapter.data[flashPosition].isShow = false
            flashAdapter.notifyItemChanged(flashPosition)
        }
    }


    private fun setupFlashImg(list: List<FastReadOption>) {
        correctPosition = Random.nextInt(0, 4)
        correctImg = list[correctPosition].title

        flashPosition = Random.nextInt(0, 9)
        flashItemList.clear()
        for (i in 0..8) {
            flashItemList.add(FastFlashItem(null, false))
        }

        flashAdapter.setList(flashItemList)
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