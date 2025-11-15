package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.result.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.service.TokenManager
import com.example.upaymimoni.domain.session.UserSession

class GoogleLoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userSession: UserSession,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(idToken: String): AuthResult<User> {
        val authResult = authRepository.loginUserWithGoogle(idToken)
        if (authResult is AuthResult.Failure) return authResult

        val user = (authResult as AuthResult.Success).data

        val existingUser = userRepository.getUser(user.id)
        if (existingUser.isSuccess) {
            userSession.setCurrentUser(user)
            return AuthResult.Success(user)
        }

        val saveResult = userRepository.saveUser(user)
        if (saveResult.isSuccess) {
            userSession.setCurrentUser(user)
            tokenManager.fetchAndSaveToken(user.id)
            return AuthResult.Success(user)
        }

        return AuthResult.Failure(
            AuthError.Internal(
                "There was an error saving the user to the database."
            )
        )
    }
}