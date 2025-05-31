package com.chatmate.domain.usecase

import com.chatmate.data.model.Message
import com.chatmate.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message): Result<Unit> {
        return chatRepository.sendMessage(message)
    }
}