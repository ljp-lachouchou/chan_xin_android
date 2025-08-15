package com.software.jetpack.compose.chan_xin_android.entity

import kotlinx.serialization.Serializable
import java.util.UUID


data class ChatLog(val id:String,val conversationId: String,val sendId:String,val recvId:String,val msgType:Int,val msgContent:String,val chatType:String,val sendTime:Long)

data class Conversation(val conversationId:String,val chatType:Int,val targetId:String,val isShow:Boolean,val seq:Long,val total:Int,val toRead:Int,val read:Int,val msg:ChatLog)


/**
 * 顶层消息帧数据类
 */
data class MessageFrame(
    val frameType: Int,
    val id: String,
    val toId: String,
    val fromId: String,
    val method: String,
    val data: MessageData
) {
    constructor(toId: String,fromId: String,data: MessageData):this(0,UUID.randomUUID().toString(),toId,fromId,"conversation.chat",data)
}

/**
 * 消息数据内容
 */
data class MessageData(
    val sendId: String,
    val recvId: String,
    val chatType: Int,
    val msg: MessageContent
)

/**
 * 消息具体内容
 */
data class MessageContent(
    val msgType: Int,
    val msgContent: String
)