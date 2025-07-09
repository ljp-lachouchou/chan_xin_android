package com.software.jetpack.compose.chan_xin_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.DefaultLifecycleObserver
import com.software.jetpack.compose.chan_xin_android.lifecycle.LoginActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.loginActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity

class LoginActivity: BaseActivity() {
    init {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            loginActivityScreen{

            }
        }
    }

    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return LoginActivityObserver()
    }
}