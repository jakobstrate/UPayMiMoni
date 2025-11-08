package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String): AuthResult<Unit> {
        val validationResult = validateInput(email)
        if (validationResult is AuthResult.Failure) {
            return AuthResult.Failure(validationResult.error)
        }

        return repo.sendResetPasswordEmail(email)
    }

    private fun validateInput(
        email: String,
    ): AuthResult<Unit> {
        if (email.trim().isEmpty()) {
            return AuthResult.Failure(
                AuthException(
                    errorType = AuthErrorType.EmptyEmail,
                    errorMessage = "Email is required"
                )
            )
        }

        return AuthResult.Success(Unit)
    }
}