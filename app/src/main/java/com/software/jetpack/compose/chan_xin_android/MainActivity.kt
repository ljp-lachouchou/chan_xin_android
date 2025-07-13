package com.software.jetpack.compose.chan_xin_android

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.datastore.dataStore
import androidx.lifecycle.DefaultLifecycleObserver
import com.software.jetpack.compose.chan_xin_android.lifecycle.MainActivityObserver

import com.software.jetpack.compose.chan_xin_android.ui.activity.MainActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity

class MainActivity : BaseActivity() {
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

