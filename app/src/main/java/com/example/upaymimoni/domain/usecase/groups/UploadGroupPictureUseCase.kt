package com.example.upaymimoni.domain.usecase.groups

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.repository.PictureStorageRepository
import com.example.upaymimoni.domain.repository.ProgressCallback

class UploadGroupPictureUseCase(
    private val storageRepository: PictureStorageRepository
) {
    suspend operator fun invoke(
        groupId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback? = null
    ) : UploadResult<String> {
        val uploadResult = storageRepository.uploadGroupPicture(groupId, uri, onProgressUpdate)

        if (uploadResult is UploadResult.Failure) {
            return uploadResult
        }

        val downloadUrl = (uploadResult as UploadResult.Success).data

        return UploadResult.Success(downloadUrl)
    }
}