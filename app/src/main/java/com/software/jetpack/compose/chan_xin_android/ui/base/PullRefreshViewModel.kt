package com.software.jetpack.compose.chan_xin_android.ui.base

import androidx.compose.animation.Animatable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PullRefreshViewModel: ViewModel() {
    val pullDistance = mutableFloatStateOf(0f)
    private val animate = androidx.compose.animation.core.Animatable(initialValue = 0f)
    private val scope = viewModelScope
    init {
        scope.launch {
            animate.animateTo(targetValue = 0f)
            pullDistance.floatValue = 0f
        }
    }
    

}