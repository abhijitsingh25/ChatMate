package com.chatmate.data.model

import kotlinx.datetime.Instant

data class Message(
    val id: String = "",
    val chatRoomId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val timestamp: Instant = Instant.DISTANT_PAST,
    val messageType: MessageType = MessageType.TEXT,
    val status: MessageStatus = MessageStatus.SENT,
    val imageUrl: String? = null
)

enum class MessageType {
    TEXT, IMAGE, SYSTEM
}

enum class MessageStatus {
    SENT, DELIVERED, READ
}