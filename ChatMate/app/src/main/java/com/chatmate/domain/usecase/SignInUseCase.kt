package com.chatmate.domain.usecase

import com.chatmate.data.model.User
import com.chatmate.domain.repository.UserRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String): Result<User> {
        return userRepository.signInUser(name)
    }
}