package com.qunyu.dividedroad.mvvm.ext

import android.text.method.LinkMovementMethod
import android.widget.TextView

/**
 * @author wzh
 * @date 2023/5/5
 */
fun TextView.buildSpannableString(init: DslSpannableStringBuilder.() -> Unit) {
    //具体实现类
    val spanStringBuilderImpl = DslSpannableStringBuilderImpl()
    spanStringBuilderImpl.init()

    //设置ClickableSpan点击事件生效
    movementMethod = LinkMovementMethod.getInstance()
    //通过实现类返回SpannableStringBuilder
    text = spanStringBuilderImpl.build()
}