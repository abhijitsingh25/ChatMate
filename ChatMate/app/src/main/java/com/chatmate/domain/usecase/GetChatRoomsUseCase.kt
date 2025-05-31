package com.chatmate.domain.usecase

import com.chatmate.data.model.ChatRoom
import com.chatmate.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatRoomsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatRoom>> {
        return chatRepository.getChatRoomsFlow()
    }
}