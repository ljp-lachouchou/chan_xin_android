package com.software.jetpack.compose.chan_xin_android.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(getDefaultLifeCycle())
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(getDefaultLifeCycle())
    }
    @Composable
    fun LifecycleComponent() {
        DisposableEffect(lifecycle.currentState) {
            val observer = getDefaultLifeCycle()
            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
    }
    abstract fun getDefaultLifeCycle() : DefaultLifecycleObserver
}
