package com.software.jetpack.compose.chan_xin_android.vm

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.http.service.ApiService
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.util.Oss
import com.software.jetpack.compose.chan_xin_android.util.PreferencesFileName
import com.software.jetpack.compose.chan_xin_android.util.StringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class UserViewmodel @Inject constructor():ViewModel() {
    private val _user = MutableStateFlow(User())
    private val _findUserInfo = MutableStateFlow<List<User>?>(listOf())
    val myUser : StateFlow<User>
        get() = _user
    var pagedUsers: Flow<PagingData<User>>? = null
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
    suspend fun findUser(
        name: String = "------1", phone: String = "1", ids: String = StringUtil.listToString(
            listOf("1")
        )
    ){
        val apiService = HttpService.getService()
        val apiResult = apiService.findUser(name, phone, ids)
        _findUserInfo.value = apiResult.data?.infos
        pagedUsers = Pager(
            config = PagingConfig(pageSize = 15),
            pagingSourceFactory = { UsersLocalPagerSource(_findUserInfo.value) }
        ).flow
            .cachedIn(viewModelScope)
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

    inner class UsersLocalPagerSource(private val findUserInfo:List<User>?):PagingSource<Int,User>() {
        override fun getRefreshKey(state: PagingState<Int, User>): Int? {
            return state.anchorPosition
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
            val position = params.key ?: 0
            val pageSize = params.loadSize
            return LoadResult.Page(
                data = findUserInfo?.subList(
                    position,
                    min(position + pageSize, findUserInfo.size)
                )?: listOf(),
                prevKey = if (position == 0) null else position - pageSize,
                nextKey = if (position + pageSize >= (findUserInfo?.size ?: 0)) null else position + pageSize
            )
        }

    }
}