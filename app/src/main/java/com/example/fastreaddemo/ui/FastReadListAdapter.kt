package com.example.fastreaddemo.ui

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastReadType

/**
 * @author wzh
 * @date 2023/5/26
 */
class FastReadListAdapter :
    BaseQuickAdapter<FastReadType, BaseViewHolder>(R.layout.fastread_item_fast_read) {
    override fun convert(holder: BaseViewHolder, item: FastReadType) {
        holder.setText(R.id.tv_title, item.gameName)
    }
}