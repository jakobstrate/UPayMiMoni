package com.example.upaymimoni.domain.usecase.expense

import android.net.Uri
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.result.AttachmentUploadResult
import com.example.upaymimoni.domain.repository.AttachmentStorageRepository
import com.example.upaymimoni.domain.repository.ExpenseRepository
import java.util.UUID

class AddExpenseWithAttachmentUseCase(
    private val expenseRepository: ExpenseRepository,
    private val attachmentRepository: AttachmentStorageRepository
) {
    suspend operator fun invoke(
        name: String,
        amount: Double,
        payerUserId: String,
        splitBetweenUserIds: List<String>,
        groupId: String,
        attachmentUri: Uri?,
        onStatusUpdate: (String) -> Unit
    ): Result<Unit> {
        if (name.isBlank() || amount <= 0 || payerUserId.isBlank() || groupId.isBlank()) {
            return Result.failure(IllegalArgumentException(
                "Expense name cannot be empty, " +
                        "amount must be positive," +
                        "user ID cannot be empty," +
                        "and Group ID cannot be empty."))
        }
        val expenseId = UUID.randomUUID().toString()
        var uploadResult: AttachmentUploadResult? = null


        if (attachmentUri != null) {
            onStatusUpdate("Starting file upload...")

            val result = attachmentRepository.uploadAttachment(
                userId = payerUserId,
                fileUri = attachmentUri,
                onStatusUpdate = onStatusUpdate
            )

            result.onSuccess {
                uploadResult = it
                onStatusUpdate("File uploaded successfully")
            }.onFailure { e ->
                // Propagate failure, potentially cleaning up partial state if necessary
                throw Exception("Failed to upload receipt file: ${e.message}", e)
            }
        }

        val newExpense = Expense(
            id = expenseId,
            name = name.trim(),
            amount = amount,
            payerUserId = payerUserId,
            splitBetweenUserIds = splitBetweenUserIds,
            groupId = groupId,
            attachmentUrl = uploadResult?.downloadUrl,
            attachmentFilename = uploadResult?.filename
        )


        return expenseRepository.addExpense(newExpense)
    }
}