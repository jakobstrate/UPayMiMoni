package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): AuthResult<User> {
        val authResult = authRepository.registerUser(email, password)

        return when (authResult) {
            is AuthResult.Success -> {
                val user = authResult.data
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

            is AuthResult.Failure -> authResult
        }
    }
}