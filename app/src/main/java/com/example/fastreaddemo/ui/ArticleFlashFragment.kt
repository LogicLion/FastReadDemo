package com.example.fastreaddemo.ui

import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentArticleFlashBinding

/**
 * 文章闪现阅读
 * @author wzh
 * @date 2023/8/28
 */
class ArticleFlashFragment : BaseFastReadFragment<FastreadFragmentArticleFlashBinding>() {
    override fun setupGameLayoutId() = R.layout.fastread_fragment_article_flash

    override fun initGameView() {
    }
}