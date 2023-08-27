package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentPartStepReadBinding
import java.lang.ref.WeakReference

/**
 * 文章分节阅读
 * @author wzh
 * @date 2023/8/27
 */
class ArticlePartStepReadFragment : BaseFastReadFragment<FastreadFragmentPartStepReadBinding>() {

    private val articleText =
        "马棚里住着 一匹小马。有一天老马对小马说“你已经长大好哇，你麦子，飞快地路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.马棚里住着 一匹小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.马棚里住着 一匹小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着达到法定的速马棚里住着 一匹小马。有一天老马对小马说“你已经长大好哇，你麦子，飞快地路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.马棚里住着 一匹小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.马棚里住着 一匹小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着坊跑去跑着跑着.小马。有一天老马对小马说“你已经长大，能妈妈做点事吗？小马连蹦带怎么不能？我很愿意帮您做事。”老马高兴地说“那好哇，你把这半口袋麦子驮到磨坊去小马驮起麦子，飞快地往磨坊跑去跑着跑着，一条小河挡住了去路，河水哗哗地流着。小马为难了，心坊跑去跑着跑着坊跑去跑着达到法定的速跑去跑着达到法定的速"


    /**
     * 步进时间
     */
    private var stepTime = 50L

    /**
     * 每次步进的字符数量
     */
    private var charPartNum = 5

    private var currCharStartIndex = 0

    private val mHandler: MyHandler = MyHandler(this)

    override fun setupGameLayoutId() = R.layout.fastread_fragment_part_step_read

    override fun initGameView() {
//        mGameBinding.tvArticle.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        mGameBinding.tvArticle.postDelayed({
            setArticleText()
        }, 500L)

    }

    private class MyHandler(fragment: ArticlePartStepReadFragment) :
        Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<ArticlePartStepReadFragment> =
            WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.setArticleText()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun setArticleText() {


        if (currCharStartIndex < articleText.length) {
            val substring = if (currCharStartIndex + charPartNum < articleText.length) {
                mHandler.sendEmptyMessageDelayed(66, stepTime)
                articleText.substring(currCharStartIndex, currCharStartIndex + charPartNum)
            } else {
                articleText.substring(currCharStartIndex, articleText.length)
            }
            currCharStartIndex += charPartNum
            mGameBinding.tvArticle.append(substring)
            mGameBinding.nestedScrollView.fullScroll(View.FOCUS_DOWN)
        }
    }
}