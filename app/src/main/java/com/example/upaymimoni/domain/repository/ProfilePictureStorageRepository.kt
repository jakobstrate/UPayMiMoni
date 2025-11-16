package com.example.upaymimoni.domain.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult

typealias ProgressCallback = (progress: Double) -> Unit

interface ProfilePictureStorageRepository {
    suspend fun uploadUserProfilePicture(
        userId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String>
}