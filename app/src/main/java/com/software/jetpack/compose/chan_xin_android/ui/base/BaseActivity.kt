package com.software.jetpack.compose.chan_xin_android.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver

abstract class BaseActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val observer = getDefaultLifeCycle()
        lifecycle.addObserver(observer)
    }

    abstract fun getDefaultLifeCycle() : DefaultLifecycleObserver

}
