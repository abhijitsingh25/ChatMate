package com.example.chatmate.model

data class Channel (
    val id: String = "",
    val name: String ,
    val createdAt: Long = System.currentTimeMillis()
)