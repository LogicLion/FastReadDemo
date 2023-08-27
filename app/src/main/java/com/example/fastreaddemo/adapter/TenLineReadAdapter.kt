package com.example.fastreaddemo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.fastreaddemo.R

/**
 * @author wzh
 * @date 2023/8/27
 */
class TenLineReadAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.fastread_item_recycler_ten_line_read) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
    }
}