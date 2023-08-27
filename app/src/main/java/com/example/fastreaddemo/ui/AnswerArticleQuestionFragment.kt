package com.example.fastreaddemo.ui

import androidx.recyclerview.widget.GridLayoutManager
import com.example.fastreaddemo.R
import com.example.fastreaddemo.adapter.SentenceOptionAdapter
import com.example.fastreaddemo.data.FastReadOption
import com.example.fastreaddemo.databinding.FastreadFragmentAnswerArticleQuestionBinding

/**
 * 文章分节阅读/文章闪现阅读/文章测试->回答文章问题
 * @author wzh
 * @date 2023/8/28
 */
class AnswerArticleQuestionFragment :
    BaseFastReadFragment<FastreadFragmentAnswerArticleQuestionBinding>() {


    private var optionList = mutableListOf<FastReadOption>()

    private val adapter: SentenceOptionAdapter = SentenceOptionAdapter()


    override fun setupGameLayoutId() = R.layout.fastread_fragment_answer_article_question

    override fun initGameView() {

        optionList.add(FastReadOption("七个个个个1字", null, 0))
        optionList.add(FastReadOption("七个个个个2字", null, 0))
        optionList.add(FastReadOption("七个个个个3字", null, 0))
        optionList.add(FastReadOption("七个个个个4字", null, 0))
        adapter.setList(optionList)
        mGameBinding.rvOption.layoutManager = object : GridLayoutManager(requireActivity(), 2) {
            override fun canScrollVertically() = false
        }
        mGameBinding.rvOption.adapter = adapter
    }
}