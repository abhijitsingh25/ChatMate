package com.chatmate.data.model

import kotlinx.datetime.Instant

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val isOnline: Boolean = false,
    val lastSeen: Instant? = null,
    val fcmToken: String = ""
)