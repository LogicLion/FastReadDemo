package com.example.fastreaddemo.ui

import android.os.Bundle
import com.example.fastreaddemo.R
import com.example.fastreaddemo.base.DataBindingActivity

/**
 * @author wzh
 * @date 2023/2/9
 */
class MainActivity : DataBindingActivity() {
    override fun setupLayoutId()= R.layout.common_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl_container, FastReadListFragment())
                .commit()
        }

    }
}