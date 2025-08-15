package com.software.jetpack.compose.chan_xin_android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import com.software.jetpack.compose.chan_xin_android.repo.ImRepository
import com.software.jetpack.compose.chan_xin_android.util.AppGlobal
import com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager.WebsocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImViewModel @Inject constructor(imRepository: ImRepository):ViewModel() {
    init {
        initWorkManager()
    }
    private fun initWorkManager() {
        viewModelScope.launch {
            val token = AppGlobal.tokenIsAva()
            withContext(Dispatchers.Main) {
                websocketManager = WebsocketManager("ws://114.215.194.88:9080/ws",token)
                websocketManager?.connect()
            }
        }
    }
    private var websocketManager:WebsocketManager? = null
    fun send(messageFrame: MessageFrame) {
        websocketManager?.sendMessage(messageFrame)
    }
    fun connect() {
        if (isConnect) return
        websocketManager?.connect() ?: {
            initWorkManager()
        }
    }
    val isConnect:Boolean
        get() = websocketManager?.isConnect ?: false
    val currentConversation = imRepository.currentConversationFlow.stateIn(
        scope = viewModelScope,
        initialValue = emptyMap(),
        started = SharingStarted.WhileSubscribed(5000)
    )

}