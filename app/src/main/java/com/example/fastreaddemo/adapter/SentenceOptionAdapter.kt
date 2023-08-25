package com.example.fastreaddemo.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastReadOption

/**
 * @author wzh
 * @date 2023/8/25
 */
class SentenceOptionAdapter :
    BaseQuickAdapter<FastReadOption, BaseViewHolder>(R.layout.fastread_item_recycler_sentence_option) {
    override fun convert(holder: BaseViewHolder, item: FastReadOption) {
        holder.setText(R.id.tv_text, item.title)

        val ivStatus = holder.getView<ImageView>(R.id.iv_status)
        if (item.status == 1) {
            ivStatus.visibility = View.VISIBLE
            ivStatus.setImageResource(R.drawable.icon_option_correct)
        } else if (item.status == 2) {
            ivStatus.visibility = View.VISIBLE
            ivStatus.setImageResource(R.drawable.icon_option_wrong)
        } else {
            ivStatus.visibility = View.INVISIBLE
        }

    }
}