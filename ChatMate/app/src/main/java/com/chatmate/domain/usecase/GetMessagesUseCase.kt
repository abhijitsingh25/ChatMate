package com.chatmate.domain.usecase

import com.chatmate.data.model.Message
import com.chatmate.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatRoomId: String): Flow<List<Message>> {
        return chatRepository.getMessagesFlow(chatRoomId)
    }
}