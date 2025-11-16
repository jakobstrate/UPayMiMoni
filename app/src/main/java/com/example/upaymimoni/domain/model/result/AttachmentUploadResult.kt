package com.example.upaymimoni.domain.model.result

/**
 * Data class to hold the result of a successful attachment upload.
 */
data class AttachmentUploadResult (
    val downloadUrl: String,
    val filename: String
)
