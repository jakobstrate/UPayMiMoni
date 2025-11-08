package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository

class LoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        val validationResult = validateInput( email, password)
        if (validationResult is AuthResult.Failure) {
            return AuthResult.Failure(validationResult.error)
        }

        return repo.loginUser(email, password)
    }

    private fun validateInput(email: String, password: String): AuthResult<Unit> {
        if (email.trim().isEmpty()) {
            return AuthResult.Failure(
                AuthException(
                    errorType = AuthErrorType.EmptyEmail,
                    errorMessage = "Email is required"
                )
            )
        }

        if (password.trim().isEmpty()) {
            return AuthResult.Failure(
                AuthException(
                    errorType = AuthErrorType.EmptyPassword,
                    errorMessage = "Password is required"
                )
            )
        }

        return AuthResult.Success(Unit)
    }
}