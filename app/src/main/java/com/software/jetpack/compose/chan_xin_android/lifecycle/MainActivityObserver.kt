package com.software.jetpack.compose.chan_xin_android.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MainActivityObserver :DefaultLifecycleObserver{
    private val logTag = javaClass.name
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.e(logTag,"onCreate")
    }
}