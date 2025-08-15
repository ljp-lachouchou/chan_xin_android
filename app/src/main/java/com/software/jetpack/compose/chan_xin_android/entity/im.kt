package com.software.jetpack.compose.chan_xin_android.entity


data class ChatLog(val id:String,val conversationId: String,val sendId:String,val recvId:String,val msgType:Int,val msgContent:String,val chatType:String,val sendTime:Long)

data class Conversation(val conversationId:String,val chatType:Int,val targetId:String,val isShow:Boolean,val seq:Long,val total:Int,val toRead:Int,val read:Int,val msg:ChatLog)