package com.software.jetpack.compose.chan_xin_android.repo

import android.util.Log
import androidx.paging.LOGGER
import com.software.jetpack.compose.chan_xin_android.cache.dao.ISocialDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialRepository @Inject constructor(private val socialDao: ISocialDao,private val userDao:IUserDao) {
    val scope = CoroutineScope(SupervisorJob())
    private val _currentUid = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentApplyFriendListFlow = _currentUid.flatMapLatest { uid->
        Log.e("SocialRepository_uid",uid)
        socialDao.getApplyFriendList(uid)
    }.catch {
        Log.e("fuck_SocialRepository_Error_uid",it.message.toString())
        flowOf<List<FriendApply>>(emptyList())
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentHandleFriendApplyListFlow = _currentUid.flatMapLatest { uid ->
        Log.e("SocialRepository_tid", uid)
        socialDao.getHandleApplyList(uid)
    }.catch {
        Log.e("fuck_SocialRepository_Error_i=tid", it.message.toString())
        flowOf<List<FriendApply>>(emptyList())
    }
    init {
        scope.launch(Dispatchers.IO) {
            val phone = AppGlobal.getUserPhone()
            userDao.getUserInfoByPhone(phone).collect{
                user->
                setCurrentUid(user.id)
            }
        }
    }
    fun setCurrentUid(uid:String) {
        _currentUid.value = uid
    }
}