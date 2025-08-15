package com.software.jetpack.compose.chan_xin_android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software.jetpack.compose.chan_xin_android.repo.ImRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ImViewModel @Inject constructor(imRepository: ImRepository):ViewModel() {
    val currentConversation = imRepository.currentConversationFlow.stateIn(
        scope = viewModelScope,
        initialValue = emptyMap(),
        started = SharingStarted.WhileSubscribed(5000)
    )
}