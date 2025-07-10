package com.software.jetpack.compose.chan_xin_android

import android.annotation.SuppressLint
import android.content.Context
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
import com.software.jetpack.compose.chan_xin_android.lifecycle.LoginActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppCoverScreen
import com.software.jetpack.compose.chan_xin_android.ui.activity.loginActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseTransBetweenScreens
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
    private val phoneKey = "phone"
    private val loginEvent:()->Unit = {
        loginService()
    }
    private val retrievePasswordEvent:()->Unit = {
        retrievePasswordService()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.slashScreen)
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Chan_xin_android)
       lifecycleScope.launch(Dispatchers.IO) {
           val phone = hasLoginHistory()
           Log.e("isHas", phone)
           withContext(Dispatchers.Main) {
               setContent {
                   LifecycleComponent()
                   if (phone == "") { //没有历史，用户第一次使用
                       NoPhone()
                   }else {
                       HasPhone(phone)
                   }
               }
           }
       }
    }
    private suspend fun hasLoginHistory():String {

        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(phoneKey)] ?: ""
        }.first() // 检查是否存在phoneKey
    }
    private fun loginService() {

    }
    private fun retrievePasswordService() {
        lifecycleScope
    }
    override fun getDefaultLifeCycle(): DefaultLifecycleObserver {
        return LoginActivityObserver()
    }
    @Composable
    fun NoPhone() {
        AppCoverScreen()
    }


    @Composable
    fun HasPhone(phone: String) {
        loginActivityScreen(phone, loginEvent = loginEvent, retrievePasswordEvent = retrievePasswordEvent)
    }

}