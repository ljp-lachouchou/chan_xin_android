package com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager

import android.util.Log
import androidx.paging.LOGGER
import com.google.gson.Gson
import com.software.jetpack.compose.chan_xin_android.entity.MessageFrame
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class WebsocketManager(url:String, token:String) {
    private var websocket:WebSocket? = null
    private val gson = Gson()
    private var isConnectBool:Boolean = false
    private val client:OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
    }
    private val request = Request.Builder()
        .url(url)
        .addHeader("Authorization",token)
        .build()
    val isConnect:Boolean
        get() = isConnectBool
    fun connect() {
        websocket = client.newWebSocket(request,object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                isConnectBool = true
                Log.e("websocket","连接成功")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.e("websocket","收到消息:$text")
                receiveMessage(text)

            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("websocket","连接失败")
                isConnectBool = false
            }
        })
    }

    fun sendMessage(message: MessageFrame) {
        val json = gson.toJson(message)
        websocket?.send(json)
    }
    fun receiveMessage(message:String) {
        val messageFrame = gson.fromJson(message,MessageFrame::class.java)
        Log.e("websocket_messageFrame",messageFrame.toString())
    }

}