package com.example.fastreaddemo.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastFlashItem

/**
 * @author wzh
 * @date 2023/8/25
 */
class PictureFlashAdapter :
    BaseQuickAdapter<FastFlashItem, BaseViewHolder>(R.layout.fastread_item_recycler_picture_flash) {
    override fun convert(holder: BaseViewHolder, item: FastFlashItem) {
        val iv = holder.getView<ImageView>(R.id.iv_target_img)

        iv.setImageResource(R.drawable.pic_anbo)
        if (item.isShow) {
            iv.visibility = View.VISIBLE
        } else {
            iv.visibility = View.INVISIBLE
        }
    }
}