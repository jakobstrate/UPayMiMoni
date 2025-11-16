package com.example.upaymimoni.domain.model

/**
 * Represents a single expense record in the app.
 *
 * @property id Unique identifier.
 * @property name Short description of the expense.
 * @property amount The total amount of the expense in DKK.
 * @property payerUserId The ID of the user who paid the expense.
 * @property groupId The ID of the group the expense is added to.
 * @property splitBetweenUserIds The ID's of users that should share expense burden.
 * @property attachmentUrl Optional attachment Url.
 * @property createdAt Timestamp for sorting.
 */
data class Expense(
    val id: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val payerUserId: String = "",
    val groupId: String = "",
    val splitBetweenUserIds: List<String> = emptyList(),
    val attachmentUrl: String? = "",
    val attachmentFilename: String? = "",
    val createdAt: Long = System.currentTimeMillis()
)