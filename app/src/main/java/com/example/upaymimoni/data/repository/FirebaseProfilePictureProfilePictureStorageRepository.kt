package com.example.upaymimoni.data.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.repository.ProfilePictureStorageRepository
import com.example.upaymimoni.domain.repository.ProgressCallback
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseProfilePictureProfilePictureStorageRepository(
    private val storage: FirebaseStorage
) : ProfilePictureStorageRepository {
    override suspend fun uploadUserProfilePicture(
        userId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String> {
        return try {
            val ref = storage.reference.child("profile_images/$userId.jpg")

            val uploadTask = ref.putFile(uri)

            onProgressUpdate?.let { callback ->
                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress: Double =
                        (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                    callback.invoke(progress)
                }
            }

            uploadTask.await()

            val downloadUrl = ref.downloadUrl.await().toString()

            UploadResult.Success(downloadUrl)
        } catch (e: Exception) {
            UploadResult.Failure(e)
        }
    }
}

