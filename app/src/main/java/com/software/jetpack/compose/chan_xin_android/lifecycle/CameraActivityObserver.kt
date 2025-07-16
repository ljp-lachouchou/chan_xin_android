package com.software.jetpack.compose.chan_xin_android.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.log

class CameraActivityObserver:DefaultLifecycleObserver {
    private val logTag = javaClass.name
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.e(logTag,"onCreate")

    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.e(logTag,"onResume")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.e(logTag,"onStart")
    }
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Log.e(logTag,"onStop")
    }
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        Log.e(logTag,"onDestroy")

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.e(logTag,"onPause")
    }
}