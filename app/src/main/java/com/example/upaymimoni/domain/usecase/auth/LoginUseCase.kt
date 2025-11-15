package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.result.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.service.TokenManager
import com.example.upaymimoni.domain.session.UserSession

class LoginUseCase(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository,
    private val userSession: UserSession,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        validateInput(email, password)?.let {
            return it
        }

        val authResult = authRepo.loginUser(email, password)
        if (authResult is AuthResult.Failure) return authResult

        val user = (authResult as AuthResult.Success).data
        val userResult = userRepo.getUser(user.id)

        return userResult.fold(
            onSuccess = { fetchedUser ->
                userSession.setCurrentUser(fetchedUser)
                tokenManager.fetchAndSaveToken(fetchedUser.id)
                AuthResult.Success(fetchedUser)
            },
            onFailure = { throwable ->
                AuthResult.Failure(
                    AuthError.Internal(
                        throwable.message ?: "Failed to fetch user from the database."
                    )
                )
            }
        )
    }

    private fun validateInput(email: String, password: String): AuthResult.Failure? {
        if (email.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyEmail)
        if (password.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyPassword)
        return null
    }
}