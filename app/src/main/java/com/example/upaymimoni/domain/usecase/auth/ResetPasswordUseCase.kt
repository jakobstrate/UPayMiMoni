package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult<Unit> {
        return repo.sendResetPasswordEmail(email)
    }
}