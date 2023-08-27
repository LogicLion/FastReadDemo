package com.example.fastreaddemo.ui

import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentArticleReadBinding

/**
 * 文章测试
 * @author wzh
 * @date 2023/8/28
 */
class ArticleReadFragment :BaseFastReadFragment<FastreadFragmentArticleReadBinding>(){
    override fun setupGameLayoutId()= R.layout.fastread_fragment_article_read

    override fun initGameView() {
    }
}