package com.example.upaymimoni.data.model

/**
 * Represents a single attachment entity, added to an expense.
 * @property id Unique identifier.
 */
data class Attachment(
    val id: String = "",
    val attachmentUrl: String? = null,
    val type: AttachmentType
)

enum class AttachmentType {
    RECEIPT, INVOICE
}

