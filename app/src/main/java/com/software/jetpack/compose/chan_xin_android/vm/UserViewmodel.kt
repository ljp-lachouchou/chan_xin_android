package com.software.jetpack.compose.chan_xin_android.vm

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewmodel @Inject constructor():ViewModel() {
    private val _user = MutableStateFlow(User())
    val myUser : StateFlow<User>
        get() = _user
    suspend fun login(phone:String,password:String):Boolean {
        if (phone == "" || password == "") {
            return false
        }
        //如果dataStore中存在就不用登录了
        val apiService = HttpService.getService()
        val loginResp = apiService.login(ApiService.LoginReq(phone = phone, password = password))
        Log.e("loginService","登录成功")
        Log.e("loginService", loginResp.data?.exp.toString())
        //dataStore将token保存
        if (loginResp.data?.token != null && loginResp.data?.exp != null) {
            AppGlobal.saveUserRela(PreferencesFileName.PHONE_KEY,phone)
        }
        if(loginResp.data?.token != null) {
            AppGlobal.saveUserRela(PreferencesFileName.USER_TOKEN,loginResp.data?.token!!)
        }
        if (loginResp.data?.exp != null) {
            AppGlobal.saveUserRela(PreferencesFileName.USER_TOKEN_EXP,864400+System.currentTimeMillis())
        }
        val userInfo = apiService.userInfo(loginResp.data?.token)
        val user = userInfo.data?.info ?: User()
        user.password = password
        Log.e("loginService",user.toString())
        UserDatabase.getInstance().userDao().saveUser(user)
        loadUser(phone)

        return true
    }
    suspend fun updateUser(sex: Int?=null,nickname: String?=null,avatar:String?=null) {
        val apiService = HttpService.getService()
        val result = apiService.updateUser(AppGlobal.tokenIsAva(), ApiService.UpdateUserReq(nickname,avatar,sex))
        Log.e("updateUser",result.data?.info.toString())
        _user.value = result.data?.info!!
        UserDatabase.getInstance().userDao().saveUser(_user.value)
    }
    fun loadUser(phone: String) {
        viewModelScope.launch {
            _user.value = UserDatabase.getInstance().userDao().getUserInfoByPhone(phone)
            Log.e("userviewmodel",myUser.value.toString())
        }
    }
    suspend fun register(nickname: String, sex: Byte, phone: String, password: String, uri: Uri?):Boolean {
        val apiService = HttpService.getService()


        if (!TextUtils.isEmpty(StringUtil.validatePassword(password))) {
            Toast.makeText(AppGlobal.getAppContext(), StringUtil.validatePassword(password), Toast.LENGTH_SHORT).show()
            return false
        }
        val avatar = Oss.uploadFile(
            System.currentTimeMillis().toString()+"."+ StringUtil.getFileExtensionFromUri(AppGlobal.getAppContext(),uri),uri)
        Log.e("registerResp",avatar)
        val registerResp =
            apiService.register(ApiService.RegisterReq(phone, password, nickname, sex, avatar.trim()))
        Log.e("registerResp",registerResp.msg)
        Log.e("registerResp",registerResp.data?.token.toString())
        return true
    }
}