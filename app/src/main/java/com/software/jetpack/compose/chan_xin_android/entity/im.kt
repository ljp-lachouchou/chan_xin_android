package com.software.jetpack.compose.chan_xin_android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity("chat_log")
data class ChatLog(
    @PrimaryKey
    val id: String,
    @ColumnInfo("conversation_id")
    val conversationId: String,
    @ColumnInfo("send_id")
    val sendId: String,
    @ColumnInfo("recv_id")
    val recvId: String,
    @ColumnInfo("msg_type")
    val msgType: Int,
    @ColumnInfo("msg_content")
    val msgContent: String,
    @ColumnInfo("chat_type")
    val chatType: Int,
    @ColumnInfo("is_local")
    val isLocal:Boolean = false,
    @ColumnInfo("send_time")
    val sendTime: Long
) {
    constructor():this("","","","",0,"",0,false,0)
}

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
    @SerializedName("ConversationId")
    val conversationId: String = "",
    @SerializedName("SendId")
    val sendId: String,
    @SerializedName("RecvId")
    val recvId: String,
    @SerializedName("ChatType")
    val chatType: Int,
    @SerializedName("Msg")
    val msg: MessageContent,
    @SerializedName("SendTime")
    val sendTime:Long = 0L
)

/**
 * 消息具体内容
 */
data class MessageContent(
    @SerializedName("MsgType")
    val msgType: Int,
    @SerializedName("MsgContent")
    val msgContent: String
)