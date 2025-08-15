package com.software.jetpack.compose.chan_xin_android.repo

import android.util.Log
import com.software.jetpack.compose.chan_xin_android.cache.dao.IUserDao
import com.software.jetpack.compose.chan_xin_android.entity.Conversation
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImRepository @Inject constructor(private val userDao: IUserDao){
    private val _currentUserId = MutableStateFlow(UserIdWithVersion("",0))
    private val scope = CoroutineScope(SupervisorJob())
    private val apiService = HttpService.getService()
    data class UserIdWithVersion(val userId:String,var version:Int)
    private var version = 0
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentConversationFlow = _currentUserId.flatMapLatest { (uid,_)->
        getConversations(uid)
    }.catch { e->
        Log.e("ImRepository_currentConversationFlow",e.toString())
        flow<Map<String,Conversation>> {
            emit(emptyMap())
        }
    }
    private fun getConversations(userId: String):Flow<Map<String,Conversation>> {
        return if (AppGlobal.isNetworkValid()) {
            flow {
                try {
                    val result = apiService.getConversations(userId).data
                    emit(result?.conversationList ?: emptyMap())
                }catch (e:Exception) {
                    android.util.Log.e("ImRepository_getConversations",e.toString())
                    emit(emptyMap())
                }
            }
        }else {
            flow {
                emit(emptyMap())
            }
        }
    }
    init {
        scope.launch(Dispatchers.IO) {
            val phone = AppGlobal.getUserPhone()
            userDao.getUserInfoByPhone(phone).collect{
                    user->
                setUserId(user.id)
            }
        }
    }
    fun setUserId(uid:String) {
        version = (version + 1) % 10
        _currentUserId.value = UserIdWithVersion(uid,version)
    }
}