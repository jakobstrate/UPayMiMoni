package com.example.upaymimoni.domain.model

import android.net.Uri

/**
 * Represents a single attachment entity, added to an expense.
 * @property id Unique identifier.
 */
data class Attachment(
    val id: String = "",
    val attachmentUrl: Uri,
    val type: AttachmentType
)

enum class AttachmentType {
    RECEIPT, INVOICE
}

