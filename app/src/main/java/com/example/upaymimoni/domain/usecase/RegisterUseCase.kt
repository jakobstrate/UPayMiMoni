package com.example.upaymimoni.domain.usecase

import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.repository.AuthRepository

class RegisterUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return repo.registerUser(email, password)
    }
}
