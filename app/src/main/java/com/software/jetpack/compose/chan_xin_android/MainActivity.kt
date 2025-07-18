package com.software.jetpack.compose.chan_xin_android

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.software.jetpack.compose.chan_xin_android.lifecycle.MainActivityObserver

import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : BaseActivity(),OnBackPressedDispatcherOwner{
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val phone = AppGlobal.getUserPhone()
            withContext(Dispatchers.Main) {
                setContent {
                    //拦截返回手势
                    LifecycleComponent()
                    MainActivityScreen()
                }
            }
        }
    }

    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return MainActivityObserver()
    }



}

