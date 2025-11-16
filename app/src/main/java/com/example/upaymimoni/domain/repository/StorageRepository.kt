package com.example.upaymimoni.domain.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult


interface StorageRepository {
    suspend fun uploadUserProfilePicture(userId: String, uri: Uri): UploadResult<String>
}