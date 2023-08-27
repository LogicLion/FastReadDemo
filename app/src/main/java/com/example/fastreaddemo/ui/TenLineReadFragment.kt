package com.example.fastreaddemo.ui

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fastreaddemo.R
import com.example.fastreaddemo.adapter.TenLineReadAdapter
import com.example.fastreaddemo.databinding.FastreadFragmentTenLineReadBinding
import com.example.fastreaddemo.ext.toast
import java.lang.ref.WeakReference

/**
 * 一目十行
 * @author wzh
 * @date 2023/8/27
 */
class TenLineReadFragment : BaseFastReadFragment<FastreadFragmentTenLineReadBinding>() {

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
    private var switchTime = 1000L


    /**
     * 列数
     */
    private var col = 4

    private val adapter1: TenLineReadAdapter = TenLineReadAdapter()
    private val adapter2: TenLineReadAdapter = TenLineReadAdapter()

    private var isTopShow = true

    private val mHandler: MyHandler = MyHandler(this)


    override fun setupGameLayoutId() = R.layout.fastread_fragment_ten_line_read

    override fun initGameView() {

        val list = mutableListOf("测试", "测试", "测试", "测试", "测试", "测试", "测试", "测试", "测试")
        adapter1.setList(list)
        mGameBinding.rv1.adapter = adapter1
        mGameBinding.rv1.layoutManager = GridLayoutManager(requireActivity(), col)

        adapter2.setList(list)
        mGameBinding.rv2.adapter = adapter2
        mGameBinding.rv2.layoutManager = GridLayoutManager(requireActivity(), col)

        mGameBinding.rv1.postDelayed({
            switchShow()
        }, 500L)
    }

    private class MyHandler(fragment: TenLineReadFragment) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<TenLineReadFragment> = WeakReference(fragment)

        override fun handleMessage(msg: Message) {
            weakReference.get()?.switchShow()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun switchShow() {
        if (isTopShow) {

            mGameBinding.rv1.visibility = View.VISIBLE
            mGameBinding.rv2.visibility = View.INVISIBLE
            if (round < roundNum) {
                round++
            } else {
                "提交".toast()
                return
            }
        } else {
            mGameBinding.rv1.visibility = View.INVISIBLE
            mGameBinding.rv2.visibility = View.VISIBLE
        }

        isTopShow = !isTopShow
        mHandler.sendEmptyMessageDelayed(33, switchTime)

    }


}