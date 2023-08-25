package com.example.fastreaddemo.ext

import android.widget.Toast
import com.example.fastreaddemo.base.App

/**
 * @author wzh
 * @date 2021/12/28
 */
private var mToast: Toast? = null

/**
 * 弹出toast
 */
fun String?.toast(){
    if (isNullOrEmpty()) {
        return
    }
    mToast?.cancel()
    val context = App.instance
    mToast = Toast.makeText(context,this,Toast.LENGTH_SHORT)
    mToast?.show()
}


