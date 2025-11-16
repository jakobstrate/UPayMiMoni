package com.example.upaymimoni.data.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.AttachmentUploadResult
import com.example.upaymimoni.domain.repository.AttachmentStorageRepository
import android.content.Context
import android.provider.OpenableColumns
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

/**
 * Concrete implementation of the StorageRepository using Firebase Storage.
 * It depends on Android Context to access ContentResolver for file metadata.
 */
class FirebaseAttachmentStorageRepository(
    private val context: Context,
    private val storage: FirebaseStorage
) : AttachmentStorageRepository {


    /**
     * Helper function to retrieve the actual file name from a Content URI using Context.
     */
    private fun getFileName(uri: Uri): String {
        var name = "unknown_file"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }

    override suspend fun uploadAttachment(
        userId: String,
        fileUri: Uri,
        onStatusUpdate: (String) -> Unit
    ): Result<AttachmentUploadResult> {
        val fileName = getFileName(fileUri)
        val uniqueFileName = "${System.currentTimeMillis()}_$fileName"

        val ref = storage.reference.child("attachments/$userId/$uniqueFileName")

        return try {
            onStatusUpdate("Starting upload...")

            ref.putFile(fileUri).await()

            val downloadUrl = ref.downloadUrl.await().toString()

            onStatusUpdate("Upload complete. Finalizing...")

            Result.success(AttachmentUploadResult(downloadUrl,fileName))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}