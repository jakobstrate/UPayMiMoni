package com.example.upaymimoni.domain.usecase;

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository

class LoginUseCase(
        private val repo: AuthRepository
) {
        suspend operator fun invoke(email: String, password: String): Result<User> {
                val result = repo.loginUser(email, password)
                return result
        }
}
