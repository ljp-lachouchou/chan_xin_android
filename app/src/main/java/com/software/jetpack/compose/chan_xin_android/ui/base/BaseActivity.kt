package com.software.jetpack.compose.chan_xin_android.ui.base

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.DefaultLifecycleObserver
import com.software.jetpack.compose.chan_xin_android.vm.SocialViewModel
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity: ComponentActivity() {
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
