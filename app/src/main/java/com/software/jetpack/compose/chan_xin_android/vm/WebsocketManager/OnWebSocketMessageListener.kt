package com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager

import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame

interface OnWebSocketMessageListener {
    fun onMessageReceive(messageFrame:MessageFrame)
}