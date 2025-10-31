package com.example.upaymimoni.data.model

/**
 * Represents a single expense record in the app.
 *
 * @property id Unique identifier.
 * @property name Short description of the expense.
 * @property amount The total amount of the expense in DKK.
 * @property payerUserId The ID of the user who paid the expense.
 * @property groupId The ID of the group the expense is added to.
 * @property attachment Optional attachment.
 * @property createdAt Timestamp for sorting.
 */
data class Expense(
    val id: String = "",
    val name: String,
    val amount: Double,
    val payerUserId: String,
    val groupId: String,
    val attachment: Attachment?,
    val createdAt: Long = System.currentTimeMillis()
)
