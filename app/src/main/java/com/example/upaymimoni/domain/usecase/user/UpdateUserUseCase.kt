package com.example.upaymimoni.domain.usecase.user

import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.model.UserUpdateResult
import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.session.UserSession

class UpdateUserUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val userSession: UserSession
) {
    suspend operator fun invoke(
        userId: String,
        newName: String,
        newEmail: String,
        newPhone: String
    ): UserUpdateResult {

        validateInput(newName, newPhone, newEmail)?.let {
            return it
        }

        val authProfileResult = authRepository.updateUserProfile(newEmail, newName)
        if (authProfileResult is UserUpdateResult.Failure) {
            return authProfileResult
        }

        val databaseProfileResult = userRepository.updateUser(
            userId = userId,
            newDisplayName = newName,
            newEmail = newEmail,
            newPhone = newPhone
        )

        if (databaseProfileResult is UserUpdateResult.Failure) {
            return databaseProfileResult
        }

        val updatedUser = (databaseProfileResult as UserUpdateResult.Success).user
        userSession.setCurrentUser(updatedUser)
        return UserUpdateResult.Success(updatedUser)
    }

    private fun validateInput(
        name: String,
        number: String,
        email: String,
    ): UserUpdateResult.Failure? {
        if (name.trim().isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidName)
        if (number.trim().isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidNumber)
        if (email.trim().isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidEmail)
        return null
    }
}