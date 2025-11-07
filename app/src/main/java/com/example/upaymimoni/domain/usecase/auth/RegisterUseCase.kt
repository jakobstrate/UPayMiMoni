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
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        val authResult = authRepository.registerUser(email, password)

        return when (authResult) {
            is AuthResult.Success -> {
                var user = authResult.data
                Log.w("RegisterUseCase", "Attempting to save user to firestore. ${user.profilePictureUrl.isNullOrEmpty()}")
                if (user.profilePictureUrl.isNullOrEmpty()) {
                    Log.w("RegisterUseCase", "User ${user.id} does not have a profile picture.")
                    val avatarUrl = avatarRepository.getRandomAvatarUrl(user.id)
                    user = user.copy(profilePictureUrl = avatarUrl)
                }
                val saveResult = userRepository.saveUser(user)
                if (saveResult.isFailure) {
                    AuthResult.Failure(AuthException(
                        errorType = AuthErrorType.InternalDataSaveError,
                        "There was an error saving the user to the database"
                    ))
                } else {
                    AuthResult.Success(user)
                }
            }

            is AuthResult.Failure -> {
                AuthResult.Failure(authResult.error)
            }
        }
    }
}