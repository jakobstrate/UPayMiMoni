package com.example.upaymimoni.domain.usecase.user

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        return userRepository.getUser(userId)
    }
}