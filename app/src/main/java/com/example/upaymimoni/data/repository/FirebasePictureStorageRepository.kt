package com.example.upaymimoni.data.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.UploadResult
import com.example.upaymimoni.domain.repository.PictureStorageRepository
import com.example.upaymimoni.domain.repository.ProgressCallback
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebasePictureStorageRepository(
    private val storage: FirebaseStorage
) : PictureStorageRepository {
    override suspend fun uploadUserProfilePicture(
        userId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String> {
        return uploadPicture(
            storagePath = "profile_images/$userId.jpg",
            uri = uri,
            onProgressUpdate = onProgressUpdate
        )
    }

    override suspend fun uploadGroupPicture(
        groupId: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String> {
        return uploadPicture(
            storagePath = "group_images/$groupId.jpg",
            uri = uri,
            onProgressUpdate = onProgressUpdate
        )
    }

    private suspend fun uploadPicture(
        storagePath: String,
        uri: Uri,
        onProgressUpdate: ProgressCallback?
    ): UploadResult<String> {
        return try {
            val ref = storage.reference.child(storagePath)

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

