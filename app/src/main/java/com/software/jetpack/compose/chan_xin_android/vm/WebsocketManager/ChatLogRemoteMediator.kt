package com.software.jetpack.compose.chan_xin_android.vm.WebsocketManager

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.software.jetpack.compose.chan_xin_android.cache.dao.IChatDao
import com.software.jetpack.compose.chan_xin_android.entity.ChatLog
import com.software.jetpack.compose.chan_xin_android.http.service.HttpService

@OptIn(ExperimentalPagingApi::class)
class ChatLogRemoteMediator(
    private val conversationId: String,
    private val dao: IChatDao,
    private val pageSize: Int
):RemoteMediator<Long,ChatLog>() {
    private val apiService = HttpService.getService()
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, ChatLog>
    ): MediatorResult {
        return try {
            val sss = state.pages.flatMap { it.data }
            Log.e("StateData", "当前 state 中的所有数据时间：${sss.map { it.sendTime }}")
            val (startTime,endTime) = when (loadType) {
                LoadType.REFRESH -> {
                    val currentTime = System.currentTimeMillis() + 60*60*1000
                    Log.e("getMaxSendTime_REFRESH","ss")
                    Pair(currentTime - 24*60*60*1000, currentTime)
                }
                LoadType.PREPEND -> {
                    val allLoadedData = state.pages.flatMap { it.data }
                    Log.e("StateData", "当前 state 中的所有数据时间：${allLoadedData.map { it.sendTime }}")
                    if (allLoadedData.isEmpty()) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    val maxSendTimeInList = allLoadedData.maxOf { it.sendTime }
                    Log.e("getMaxSendTime_PREPEND",maxSendTimeInList.toString())
                    Pair(maxSendTimeInList+1,System.currentTimeMillis())
                }
                LoadType.APPEND -> {
                    val allLoadedData = state.pages.flatMap { it.data }
                    Log.e("StateData", "当前 state 中的所有数据时间：${allLoadedData.map { it.sendTime }}")
                    if (allLoadedData.isEmpty()) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    val minSendTimeInList = allLoadedData.minOf { it.sendTime }
                    Log.e("getMaxSendTime_APPEND",minSendTimeInList.toString())
                    Pair(0L,minSendTimeInList - 1)
                }
            }
            Log.e("getMaxSendTime_endTime",endTime.toString())
            val response = apiService.getChatLog(
                conversationId = conversationId,
                startSendTime = startTime,
                endSendTime = endTime,
                count = pageSize,
                msgId = ""
            )
            val remoteLogs = response.data?.list ?: emptyList()
            Log.e("RemoteAPPEND", "查询范围：[$startTime, $endTime]，API返回 ${remoteLogs.size} 条数据,$remoteLogs")
            if (remoteLogs.isNotEmpty()) {
                dao.saveChatLogs(remoteLogs.distinctBy { it.id })
            }
            val endOfPagination = remoteLogs.isEmpty()
            Log.e("RemoteAPPEND", "是否终止加载：$endOfPagination") //19:35:39.873
            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            MediatorResult.Error(e)//19:28:00.313  19:28:00.147 19:28:00.150
        }
    }
}