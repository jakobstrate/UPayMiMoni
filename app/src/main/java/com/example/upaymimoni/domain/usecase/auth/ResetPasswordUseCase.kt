package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.result.AuthResult
import com.example.upaymimoni.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult<Unit> {
        validateInput(email)?.let {
            return it
        }

        return authRepo.sendResetPasswordEmail(email)
    }

    private fun validateInput(email: String): AuthResult.Failure? {
        if (email.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyEmail)
        return null
    }
}