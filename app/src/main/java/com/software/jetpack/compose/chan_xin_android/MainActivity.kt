package com.software.jetpack.compose.chan_xin_android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import com.software.jetpack.compose.chan_xin_android.lifecycle.MainActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(),OnBackPressedDispatcherOwner{
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityScreen()
        }
    }

    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return MainActivityObserver()
    }



}

