package com.chatmate.data.model

import kotlinx.datetime.Instant

data class ChatRoom(
    val id: String = "",
    val name: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: Message? = null,
    val lastMessageTimestamp: Instant = Instant.DISTANT_PAST,
    val createdAt: Instant = Instant.DISTANT_PAST,
    val unreadCount: Int = 0,
    val isTyping: List<String> = emptyList() // User IDs who are typing
)