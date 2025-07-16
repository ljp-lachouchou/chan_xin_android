package com.software.jetpack.compose.chan_xin_android.ui.base

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.ContentView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity: ComponentActivity() {
    internal val sharedViewModel: UserViewmodel by viewModels<UserViewmodel>()
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
