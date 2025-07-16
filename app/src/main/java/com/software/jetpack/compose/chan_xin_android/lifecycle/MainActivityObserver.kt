package com.software.jetpack.compose.chan_xin_android.lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppTopBar
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.launch

class MainActivityObserver(private val vm:UserViewmodel) :DefaultLifecycleObserver{
    private val logTag = javaClass.name
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Log.e(logTag,"onCreate")
        owner.lifecycleScope.launch { vm.loadUser(AppGlobal.hasPhoneHistory()) }
    }
}