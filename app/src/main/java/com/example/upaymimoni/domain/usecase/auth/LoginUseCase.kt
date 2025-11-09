package com.example.upaymimoni.domain.usecase.auth

import android.util.Log
import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository

class LoginUseCase(
    private val repo: AuthRepository,
    private val userRepo: UserRepository,
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        val validationResult = validateInput(email, password)
        if (validationResult is AuthResult.Failure) {
            return AuthResult.Failure(validationResult.error)
        }

        val authResult = repo.loginUser(email, password)

        return when (authResult) {
            is AuthResult.Success -> {
                val user = authResult.data
                Log.d(
                    "LoginUseCase",
                    "Attempting to gather user information from database. ${user.id}"
                )
                val fetchResult = userRepo.getUser(user.id)

                fetchResult.fold(
                    onSuccess = { fetchedUser ->
                        AuthResult.Success(fetchedUser)
                    },
                    onFailure = { throwable ->
                        AuthResult.Failure(
                            AuthException(
                                errorType = AuthErrorType.InternalDataSaveError,
                                errorMessage = throwable.message
                            )
                        )
                    }
                )
            }

            is AuthResult.Failure -> {
                AuthResult.Failure(authResult.error)
            }
        }
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