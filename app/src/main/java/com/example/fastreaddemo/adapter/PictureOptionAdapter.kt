package com.example.fastreaddemo.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.fastreaddemo.R
import com.example.fastreaddemo.data.FastReadOption
import com.example.fastreaddemo.ext.dp

/**
 * @author wzh
 * @date 2023/8/25
 */
class PictureOptionAdapter :BaseQuickAdapter<FastReadOption,BaseViewHolder>(R.layout.fastread_item_recycler_picture_option) {
    override fun convert(holder: BaseViewHolder, item: FastReadOption) {
    }


    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {

        val ivOption = viewHolder.getView<ImageView>(R.id.iv_option)
        val itemWidth = 100.dp

            viewHolder.itemView.layoutParams.width = itemWidth
            viewHolder.itemView.layoutParams.height = itemWidth
//            val params = ivMenu.layoutParams as ConstraintLayout.LayoutParams
//            params.setMargins(15.dp, 0, 15.dp, 0)
//            ivMenu.layoutParams = params

    }
}