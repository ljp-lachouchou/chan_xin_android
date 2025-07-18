package com.software.jetpack.compose.chan_xin_android.repo

import android.util.Log
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.entity.User
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao:IUserDao) {
    private val _currentPhone = MutableStateFlow("")
    private val scope = CoroutineScope(SupervisorJob())
    init {
        scope.launch(Dispatchers.IO) {
            _currentPhone.value = AppGlobal.hasPhoneHistory()
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUserFlow :Flow<User> = _currentPhone
        .flatMapLatest { phone-> //如果_currentPhone更新，流会发生变化
            Log.e("UserRepository",phone)
            userDao.getUserInfoByPhone(phone)
        }.catch {e->
            Log.e("UserRepository", "获取用户数据失败: ${e.message}")
            flowOf(User()) // 出错时返回空用户
        }
    fun setPhone(phone:String) {
        _currentPhone.value = phone
        Log.e("UserR",_currentPhone.value)
    }
}