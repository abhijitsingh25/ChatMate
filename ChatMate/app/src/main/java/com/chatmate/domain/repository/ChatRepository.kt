package com.chatmate.domain.repository

import com.chatmate.data.model.ChatRoom
import com.chatmate.data.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatRoomsFlow(): Flow<List<ChatRoom>>
    fun getMessagesFlow(chatRoomId: String): Flow<List<Message>>
    suspend fun sendMessage(message: Message): Result<Unit>
    suspend fun createChatRoom(participantIds: List<String>): Result<ChatRoom>
    suspend fun markMessagesAsRead(chatRoomId: String, userId: String)
    suspend fun setTypingStatus(chatRoomId: String, userId: String, isTyping: Boolean)
    suspend fun updateMessageStatus(messageId: String, status: com.chatmate.data.model.MessageStatus)
}