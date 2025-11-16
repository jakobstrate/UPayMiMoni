package com.example.upaymimoni.data.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseStorageRepository(
    private val storage: FirebaseStorage
) : StorageRepository {
    override suspend fun uploadUserProfilePicture(
        userId: String,
        uri: Uri
    ): UploadResult<String> {
        return try {
            val ref = storage.reference.child("profile_images/$userId.jpg")

            ref.putFile(uri).await()

            val downloadUrl = ref.downloadUrl.await().toString()

            UploadResult.Success(downloadUrl)
        } catch (e: Exception) {
            UploadResult.Failure(e)
        }
    }
}