package com.example.fastreaddemo.ui

import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentSkipReadTrainBinding
import com.example.fastreaddemo.ext.toast
import com.example.fastreaddemo.widget.SkipReadTrainView

/**
 * @author wzh
 * @date 2023/8/17
 */
class SkipReadTrainFragment : BaseFastReadFragment<FastreadFragmentSkipReadTrainBinding>() {
    override fun setupGameLayoutId() = R.layout.fastread_fragment_skip_read_train

    override fun initGameView() {
        mGameBinding.skipReadView.setCellNum(3, 6 ,6)
        mGameBinding.skipReadView.setGridListener(object :SkipReadTrainView.OnGridListener{
            override fun onClick(itemClick: String) {
            }

            override fun onFinish() {
                "完成".toast()
            }

        })
    }
}