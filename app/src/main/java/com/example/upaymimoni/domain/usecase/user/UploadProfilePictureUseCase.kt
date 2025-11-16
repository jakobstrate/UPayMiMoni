package com.example.upaymimoni.domain.usecase.user

import android.net.Uri
import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.model.result.UserUpdateResult
import com.example.upaymimoni.domain.repository.ProfilePictureStorageRepository
import com.example.upaymimoni.domain.repository.UserRepository
import com.example.upaymimoni.domain.session.UserSession

class UploadProfilePictureUseCase(
    private val storageRepository: ProfilePictureStorageRepository,
    private val userRepository: UserRepository,
    private val userSession: UserSession
) {
    suspend operator fun invoke(userId: String, uri: Uri): UserUpdateResult {
        val uploadResult = storageRepository.uploadUserProfilePicture(userId, uri)

        if (uploadResult is UploadResult.Failure) {
            return UserUpdateResult.Failure(
                UpdateUserError.NetworkUnavailable
            )
        }

        val downloadUrl = (uploadResult as UploadResult.Success).data

        val databaseProfileResult = userRepository.updateUserProfilePicture(userId, downloadUrl)

        if (databaseProfileResult is UserUpdateResult.Failure) {
            return databaseProfileResult
        }

        val updatedUser = (databaseProfileResult as UserUpdateResult.Success).user
        userSession.setCurrentUser(updatedUser)
        return UserUpdateResult.Success(updatedUser)
    }
}