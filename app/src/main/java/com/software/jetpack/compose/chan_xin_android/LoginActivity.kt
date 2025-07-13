package com.software.jetpack.compose.chan_xin_android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.lifecycle.LoginActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppCoverScreen
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppCoverScreenNav
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseTransBetweenScreens
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.PHONE_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class LoginActivity: BaseActivity() {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "users")
    private val loginEvent:()->Unit = {
        loginService()
    }
    private val retrievePasswordEvent:()->Unit = {
        retrievePasswordService()
    }
    private val registerEvent:()->Unit = {
        registerService()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.slashScreen)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Chan_xin_android)

       lifecycleScope.launch(Dispatchers.IO) {
           val phone = hasLoginHistory()
           val token = AppGlobal.tokenIsAva()
           Log.e("isHas", phone)
           withContext(Dispatchers.Main) {
               setContent {
                   LifecycleComponent()
                   if (phone == "") { //没有历史，用户第一次使用
                       NoPhone()
                   }else {
                       if (token == "") HasPhone(phone)
                       else {
                           val intent = Intent(this@LoginActivity,MainActivity::class.java)
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 清除目标 Activity 之上的所有 Activity
                           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // 创建新任务（通常与 CLEAR_TOP 一起使用）
                           this@LoginActivity.startActivity(intent)
                           this@LoginActivity.finish()
                       }

                   }
               }
           }
       }
    }
    private suspend fun hasLoginHistory():String {
//        AppGlobal.saveUserRela(PHONE_KEY,"")
        return AppGlobal.hasPhoneHistory()
    }
    private fun loginService() {

    }
    private fun retrievePasswordService() {
        lifecycleScope
    }
    private fun registerService() {

    }
    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return LoginActivityObserver()
    }
    @Composable
    fun NoPhone() {
        AppCoverScreenNav()
    }


    @Composable
    fun HasPhone(phone: String) {
        LoginActivityScreen(phone)
    }

}