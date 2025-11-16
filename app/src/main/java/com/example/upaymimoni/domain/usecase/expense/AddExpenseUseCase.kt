package com.example.upaymimoni.domain.usecase.expense

import com.example.upaymimoni.domain.model.Attachment
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.repository.ExpenseRepository
import java.util.UUID

/**
 * A Use Case representing the business rule for adding an expense.
 */
class AddExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(
        name: String,
        amount: Double,
        payerUserId: String,
        splitBetweenUserIds: List<String>,
        groupId: String,
        attachmentUrl: String? = ""
    ): Result<Unit> {
        if (name.isBlank() || amount <= 0 || payerUserId.isBlank() || groupId.isBlank()) {
            return Result.failure(IllegalArgumentException(
                "Expense name cannot be empty, " +
                        "amount must be positive," +
                        "user ID cannot be empty," +
                        "and Group ID cannot be empty."))
        }

        val newExpense = Expense(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            amount = amount,
            payerUserId = payerUserId,
            splitBetweenUserIds = splitBetweenUserIds,
            groupId = groupId,
            attachmentUrl = attachmentUrl
        )

        //TODO, when groups are made, add expense to group on success

        return expenseRepository.addExpense(newExpense)
    }
}