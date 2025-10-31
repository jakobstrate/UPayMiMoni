package com.example.upaymimoni.domain.usecase

import com.example.upaymimoni.data.model.Attachment
import com.example.upaymimoni.data.model.Expense
import com.example.upaymimoni.data.repository.ExpenseRepository

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
        groupId: String,
        attachment: Attachment?
    ): Result<Unit> {
        if (name.isBlank() || amount <= 0 || payerUserId.isBlank() || groupId.isBlank()) {
            return Result.failure(IllegalArgumentException(
                "Expense name cannot be empty, " +
                        "amount must be positive," +
                        "user ID cannot be empty," +
                        "and Group ID cannot be empty."))
        }

        val newExpense = Expense(
            name = name.trim(),
            amount = amount,
            payerUserId = payerUserId,
            groupId = groupId,
            attachment = attachment
        )

        return expenseRepository.addExpense(newExpense)
    }
}