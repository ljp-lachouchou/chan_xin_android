package com.software.jetpack.compose.chan_xin_android.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.log

class LoginActivityObserver:DefaultLifecycleObserver {
    private val logTag = javaClass.name
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e(logTag,"onCreate")
    }
}