package com.example.upaymimoni.domain.usecase.user

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.repository.ProfilePictureStorageRepository
import com.example.upaymimoni.domain.repository.ProgressCallback

class UploadProfilePictureUseCase(
    private val storageRepository: ProfilePictureStorageRepository,
) {
    suspend operator fun invoke(
        userId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback? = null
    ): UploadResult<String> {
        val uploadResult = storageRepository.uploadUserProfilePicture(userId, uri, onProgressUpdate)

        if (uploadResult is UploadResult.Failure) {
            return uploadResult
        }

        val downloadUrl = (uploadResult as UploadResult.Success).data

        return UploadResult.Success(downloadUrl)
    }
}