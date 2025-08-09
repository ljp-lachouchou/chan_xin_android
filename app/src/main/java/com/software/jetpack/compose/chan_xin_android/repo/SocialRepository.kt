package com.software.jetpack.compose.chan_xin_android.repo

import android.util.Log
import androidx.paging.LOGGER
import com.software.jetpack.compose.chan_xin_android.cache.dao.ISocialDao
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.cache.database.UserDatabase
import com.software.jetpack.compose.chan_xin_android.entity.Friend
import com.software.jetpack.compose.chan_xin_android.entity.FriendApply
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialRepository @Inject constructor(val socialDao: ISocialDao,private val userDao:IUserDao) {
    data class UserIdWithVersion(val userId:String,var version:Int)
    private var version = 0
    val scope = CoroutineScope(SupervisorJob())
    private val _currentUid = MutableStateFlow(UserIdWithVersion("",0))
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentApplyFriendListFlow = _currentUid.flatMapLatest { (uid,_)->
        Log.e("SocialRepository_uid",uid)
        socialDao.getApplyFriendList(uid)
    }.catch {
        Log.e("fuck_SocialRepository_Error_uid",it.message.toString())
        flowOf<List<FriendApply>>(emptyList())
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentFriendListFlow = _currentUid.flatMapLatest { (uid,version)->
        Log.e("SocialRepository_uid","$uid,$version")
        Log.e("SocialRepository_uid_data",socialDao.getFriendList(uid).first().toString())
        socialDao.getFriendList(uid)
    }.catch {
        Log.e("fuck_SocialRepository_Error_friend",it.toString())
        flowOf<List<Friend>>(emptyList())
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentHandleFriendApplyListFlow = _currentUid.flatMapLatest { (uid,_) ->
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
        version = (version + 1) % 10
        _currentUid.value = UserIdWithVersion(uid,version)
    }
}