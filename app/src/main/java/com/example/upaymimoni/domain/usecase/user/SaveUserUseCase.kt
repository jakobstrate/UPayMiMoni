package com.example.upaymimoni.domain.usecase.user

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        userRepository.saveUser(user)
    }
}