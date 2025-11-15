package com.example.upaymimoni.domain.usecase.auth

import android.util.Log
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.result.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.AvatarRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.service.TokenManager
import com.example.upaymimoni.domain.session.UserSession

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val avatarRepository: AvatarRepository,
    private val userSession: UserSession,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(
        name: String,
        number: String,
        email: String,
        password: String
    ): AuthResult<User> {
        validateInput(name, number, email, password)?.let {
            return it
        }

        val authResult = authRepository.registerUser(email, password)
        if (authResult is AuthResult.Failure) return authResult

        val baseUser = (authResult as AuthResult.Success).data

        val completeUser = getFullUser(baseUser, name, number)

        val saveResult = userRepository.saveUser(completeUser)

        return if (saveResult.isSuccess) {
            userSession.setCurrentUser(completeUser)
            tokenManager.fetchAndSaveToken(completeUser.id)
            AuthResult.Success(completeUser)
        } else {
            AuthResult.Failure(
                AuthError.Internal("There was an error saving the user to the database")
            )
        }
    }

    private fun validateInput(
        name: String,
        number: String,
        email: String,
        password: String
    ): AuthResult.Failure? {
        if (name.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyName)
        if (number.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyNumber)
        if (email.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyEmail)
        if (password.trim().isBlank()) return AuthResult.Failure(AuthError.EmptyPassword)
        return null
    }

    private suspend fun getFullUser(
        user: User,
        name: String,
        number: String
    ): User {
        val updatedUser = user.copy(
            displayName = name.trim(),
            phoneNumber = number.trim()
        )

        if (!updatedUser.profilePictureUrl.isNullOrEmpty()) {
            return updatedUser
        }

        val avatarUrl = avatarRepository.getRandomAvatarUrl(user.id)
        return updatedUser.copy(profilePictureUrl = avatarUrl)
    }
}