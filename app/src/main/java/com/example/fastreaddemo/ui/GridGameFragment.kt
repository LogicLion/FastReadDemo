package com.example.fastreaddemo.ui

import android.view.View
import android.widget.Toast
import com.example.fastreaddemo.R
import com.example.fastreaddemo.databinding.FastreadFragmentGridGameBinding
import com.example.fastreaddemo.widget.CustomGridView

/**
 * 舒尔特方格
 * @author wzh
 * @date 2023/8/11
 */
class GridGameFragment : BaseFastReadFragment<FastreadFragmentGridGameBinding>() {
    override fun setupGameLayoutId() = R.layout.fastread_fragment_grid_game

    override fun initGameView() {

        getTvSwitchScreenOrientation().visibility = View.GONE
        val textList = mutableListOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K")

        mGameBinding.customGridView.setTextList(textList, 5, 6)

        mGameBinding.customGridView.setGridListener(object : CustomGridView.OnGridListener {
            override fun onClick(position: Int) {
            }

            override fun onFinish() {
                Toast.makeText(requireActivity(), "完成", Toast.LENGTH_SHORT).show()
            }
        })

    }
}