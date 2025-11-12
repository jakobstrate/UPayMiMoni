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
        if (newName.isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidName)
        if (newEmail.isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidEmail)
        if (newPhone.isBlank()) return UserUpdateResult.Failure(UpdateUserError.InvalidNumber)

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
}