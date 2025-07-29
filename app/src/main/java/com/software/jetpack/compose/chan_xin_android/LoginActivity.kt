package com.software.jetpack.compose.chan_xin_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.lifecycleScope
import com.software.jetpack.compose.chan_xin_android.lifecycle.LoginActivityObserver
import com.software.jetpack.compose.chan_xin_android.ui.activity.AppCoverScreenNav
import com.software.jetpack.compose.chan_xin_android.ui.activity.LoginActivityScreen
import com.software.jetpack.compose.chan_xin_android.ui.base.BaseActivity
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName.PHONE_KEY
import com.software.jetpack.compose.chan_xin_android.vm.UserViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                   if (phone == "") { //没有历史，用户第一次使用
                       NoPhone()
                   }else {
//                       HasPhone(sharedViewModel,phone)
                       if (token == "") HasPhone(phone = phone)
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
    fun NoPhone(sharedViewModel: UserViewmodel= hiltViewModel()) {
        AppCoverScreenNav(sharedViewModel)
    }


    @Composable
    fun HasPhone(vm:UserViewmodel= hiltViewModel(), phone: String) {
        LoginActivityScreen(vm,phone)
    }

}