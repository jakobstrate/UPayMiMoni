package com.example.upaymimoni.domain.repository

import android.net.Uri
import com.example.upaymimoni.domain.model.result.AttachmentUploadResult

/**
 * Interface defining the contract for handling file storage operations,
 * such as receipt uploading.
 */
interface AttachmentStorageRepository {
    /**
     * Uploads a file (like a receipt image) associated with a user to storage.
     * @param userId The ID of the user uploading the file.
     * @param fileUri The content URI of the file on the device.
     * @param onStatusUpdate Callback for reporting upload progress/status.
     * @return Result containing the URL and filename upon successful upload.
     */
    suspend fun uploadAttachment(
        userId: String,
        fileUri: Uri,
        onStatusUpdate: (String) -> Unit
    ): Result<AttachmentUploadResult>
}