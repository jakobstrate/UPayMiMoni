package com.example.upaymimoni.domain.usecase.expense

import com.example.upaymimoni.domain.repository.ExpenseRepository

/**
 * A Use Case representing the business rule for removing an expense.
 */
class RemoveExpenseUseCase(
    private val expenseRepository: ExpenseRepository
) {
    suspend operator fun invoke(expenseId: String): Result<Unit> {
        return expenseRepository.removeExpense(expenseId)
    }
}