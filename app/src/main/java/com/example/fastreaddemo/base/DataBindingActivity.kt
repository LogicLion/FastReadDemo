package com.example.fastreaddemo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider


abstract class DataBindingActivity: AppCompatActivity() {
    private lateinit var mActivityProvider: ViewModelProvider
    private var mFactory: ViewModelProvider.Factory? = null
    private var mBinding: ViewDataBinding? = null
    protected val TAG = this.javaClass.simpleName

    /**
     * 布局id,ViewModel在布局里绑定的id请统一设置成"vm"
     */
    protected abstract fun setupLayoutId(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ViewDataBinding =
            DataBindingUtil.setContentView(this, setupLayoutId())
        binding.lifecycleOwner = this

        mBinding = binding
    }


    fun <T : ViewDataBinding> getViewBinding(): T {
        return mBinding as T
    }

}

