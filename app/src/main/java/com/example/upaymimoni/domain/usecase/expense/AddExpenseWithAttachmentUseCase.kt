package com.example.upaymimoni.domain.usecase.expense

import android.net.Uri
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.result.AttachmentUploadResult
import com.example.upaymimoni.domain.repository.AttachmentStorageRepository
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import java.lang.Compiler.command
import java.util.UUID

class AddExpenseWithAttachmentUseCase(
    private val expenseRepository: ExpenseRepository,
    private val attachmentRepository: AttachmentStorageRepository,
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(
        expense: Expense,
        attachmentUri: Uri?,
        onStatusUpdate: (String) -> Unit
    ): Result<Unit> {
        if (expense.name.isBlank() || expense.amount <= 0 || expense.payerUserId.isBlank() || expense.groupId.isBlank()) {
            return Result.failure(IllegalArgumentException(
                "Expense name cannot be empty, " +
                        "amount must be positive," +
                        "user ID cannot be empty," +
                        "and Group ID cannot be empty."))
        }
        val expenseId = UUID.randomUUID().toString()
        var uploadResult: AttachmentUploadResult? = null
        var attachmentFilenameForWiping: String? = null

        if (attachmentUri != null) {
            onStatusUpdate("Starting file upload")

            val result = attachmentRepository.uploadAttachment(
                userId = expense.payerUserId,
                fileUri = attachmentUri,
                onStatusUpdate = onStatusUpdate
            )

            result.onSuccess {
                uploadResult = it
                attachmentFilenameForWiping = it.filename
                onStatusUpdate("File uploaded successfully")
            }.onFailure { e ->
                throw Exception("Failed to upload attachment file: ${e.message}", e)
            }
        }

        val newExpense = Expense(
            id = expenseId,
            name = expense.name.trim(),
            amount = expense.amount,
            payerUserId = expense.payerUserId,
            splitBetweenUserIds = expense.splitBetweenUserIds,
            groupId = expense.groupId,
            attachmentUrl = uploadResult?.downloadUrl,
            attachmentFilename = uploadResult?.filename
        )

        onStatusUpdate("Saving expense")
        val saveExpenseResult = expenseRepository.addExpense(newExpense)

        saveExpenseResult.onFailure { e ->
            wipeYourAssWithIt(
                expenseId,
                attachmentFilenameForWiping,
                onStatusUpdate
            )
            return Result.failure(Exception("Failed to save expense: ${e.message}", e))
        }

        onStatusUpdate("Linking expense to group: ${newExpense.groupId}")
        val addExpenseToGroupResult = groupRepository.addExpenseToGroup(
            groupId = newExpense.groupId,
            expenseId = newExpense.id
        )

        addExpenseToGroupResult.onFailure { e ->
            wipeYourAssWithIt(
                expenseId,
                attachmentFilenameForWiping,
                onStatusUpdate
            )
        }

        onStatusUpdate("Expense successfully added and linked to group.")
        return Result.success(Unit)
    }

    private suspend fun wipeYourAssWithIt(
        expenseId: String?,
        attachmentFilename: String?,
        onStatusUpdate: (String) -> Unit
    ) {
        onStatusUpdate("Failure detected. Initiating wipe of behind")

        // Rollback Attachment
        if (attachmentFilename != null) {
            attachmentRepository.deleteAttachment(attachmentFilename)
                .onSuccess { onStatusUpdate("Attachment file wiped across ass successfuly.") }
                .onFailure { onStatusUpdate("Warning: Could not wipe attachment file: ${it.message}") }
        }

        // Rollback Expense
        if (expenseId != null) {
            expenseRepository.removeExpense(expenseId)
                .onSuccess { onStatusUpdate("Expense data rollback successful.") }
                .onFailure { onStatusUpdate("Warning: Could not wipe expense: ${it.message}") }
        }
        onStatusUpdate("Wiping completed.")
    }
}