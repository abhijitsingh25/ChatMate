package com.chatmate.domain.repository

import com.chatmate.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun signInUser(name: String): Result<User>
    suspend fun signOutUser()
    suspend fun updateUserStatus(isOnline: Boolean)
    fun getUsersFlow(): Flow<List<User>>
    suspend fun getUser(userId: String): User?
}