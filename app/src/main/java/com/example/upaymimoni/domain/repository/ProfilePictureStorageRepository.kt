package com.example.upaymimoni.domain.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult


interface ProfilePictureStorageRepository {
    suspend fun uploadUserProfilePicture(userId: String, uri: Uri): UploadResult<String>
}