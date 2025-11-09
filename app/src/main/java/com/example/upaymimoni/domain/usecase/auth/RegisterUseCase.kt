package com.example.upaymimoni.domain.usecase.auth

import android.util.Log
import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.AvatarRepository
import com.example.upaymimoni.domain.repository.UserRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val avatarRepository: AvatarRepository
) {
    suspend operator fun invoke(
        name: String,
        number: String,
        email: String,
        password: String
    ): AuthResult<User> {
        val validationResult = validateInput(name, number, email, password)
        if (validationResult is AuthResult.Failure) {
            return AuthResult.Failure(validationResult.error)
        }

        val authResult = authRepository.registerUser(email, password)

        return when (authResult) {
            is AuthResult.Success -> {
                val user = authResult.data
                Log.d(
                    "RegisterUseCase",
                    "Attempting to save user to firestore. ${user.profilePictureUrl.isNullOrEmpty()}"
                )
                val saveUser = getFullUser(user, name, number)
                val saveResult = userRepository.saveUser(saveUser)
                if (saveResult.isFailure) {
                    AuthResult.Failure(
                        AuthException(
                            errorType = AuthErrorType.InternalDataSaveError,
                            "There was an error saving the user to the database"
                        )
                    )
                } else {
                    AuthResult.Success(saveUser)
                }
            }

            is AuthResult.Failure -> {
                AuthResult.Failure(authResult.error)
            }
        }
    }

    private fun validateInput(
        name: String,
        number: String,
        email: String,
        password: String,
    ): AuthResult<Unit> {
        if (name.trim().isEmpty()) {
            return AuthResult.Failure(
                AuthException(
                    errorType = AuthErrorType.EmptyName,
                    errorMessage = "Name is required"
                )
            )
        }

        if (number.trim().isEmpty()) {
            return AuthResult.Failure(
                AuthException(
                    errorType = AuthErrorType.EmptyNumber,
                    errorMessage = "Number is required"
                )
            )
        }

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

    private suspend fun getFullUser(user: User, name: String, number: String): User {
        var updatedUser = user.copy(
            displayName = name,
            phoneNumber = number
        )
        if (user.profilePictureUrl.isNullOrEmpty()) {
            Log.v("RegisterUseCase", "User ${user.id} does not have a profile picture.")
            val avatarUrl = avatarRepository.getRandomAvatarUrl(user.id)
            updatedUser = updatedUser.copy(profilePictureUrl = avatarUrl)
        }

        return updatedUser
    }
}