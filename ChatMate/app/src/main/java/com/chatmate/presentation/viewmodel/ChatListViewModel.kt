package com.chatmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatmate.data.model.ChatRoom
import com.chatmate.domain.usecase.GetChatRoomsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatRoomsUseCase: GetChatRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    init {
        loadChatRooms()
    }

    private fun loadChatRooms() {
        viewModelScope.launch {
            getChatRoomsUseCase()
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load chats"
                    )
                }
                .collect { chatRooms ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        chatRooms = chatRooms,
                        error = null
                    )
                }
        }
    }

    fun retryLoading() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        loadChatRooms()
    }
}

data class ChatListUiState(
    val isLoading: Boolean = true,
    val chatRooms: List<ChatRoom> = emptyList(),
    val error: String? = null
)