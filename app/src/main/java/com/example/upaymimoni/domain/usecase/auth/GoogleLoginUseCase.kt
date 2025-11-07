package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository

class GoogleLoginUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(idToken: String): AuthResult<User> {
        val authResult = authRepository.loginUserWithGoogle(idToken)

        return when (authResult) {
            is AuthResult.Success -> {
                val user = authResult.data

                val existingUser = userRepository.getUser(user.id)

                if (existingUser.isSuccess) {
                    AuthResult.Success(user)
                } else {
                    val saveResult = userRepository.saveUser(user)
                    if (saveResult.isSuccess) {
                        AuthResult.Success(user)
                    } else {
                        AuthResult.Failure(AuthException(
                            errorType = AuthErrorType.InternalDataSaveError,
                            "There was an error saving the user to the database"
                        ))
                    }
                }
            }

            is AuthResult.Failure -> {
                AuthResult.Failure(authResult.error)
            }
        }
    }
}