package com.example.upaymimoni.domain.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult

typealias ProgressCallback = (progress: Double) -> Unit

interface PictureStorageRepository {
    suspend fun uploadUserProfilePicture(
        userId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String>

    suspend fun uploadGroupPicture(
        groupId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String>
}