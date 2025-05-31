package com.chatmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chatmate.data.model.Message
import com.chatmate.data.model.MessageType
import com.chatmate.domain.usecase.GetCurrentUserUseCase
import com.chatmate.domain.usecase.GetMessagesUseCase
import com.chatmate.domain.usecase.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var currentChatRoomId: String = ""
    private var currentUser: com.chatmate.data.model.User? = null

    fun initializeChat(chatRoomId: String) {
        currentChatRoomId = chatRoomId

        viewModelScope.launch {
            currentUser = getCurrentUserUseCase()
            loadMessages()
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            getMessagesUseCase(currentChatRoomId)
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load messages"
                    )
                }
                .collect { messages ->
                    _uiState.value = _uiState.value.copy(
                        messages = messages,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }

    fun sendMessage(content: String) {
        val user = currentUser ?: return
        if (content.isBlank()) return

        val message = Message(
            chatRoomId = currentChatRoomId,
            senderId = user.id,
            senderName = user.name,
            content = content.trim(),
            messageType = MessageType.TEXT
        )

        viewModelScope.launch {
            sendMessageUseCase(message)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to send message"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ChatUiState(
    val isLoading: Boolean = true,
    val messages: List<Message> = emptyList(),
    val error: String? = null
)