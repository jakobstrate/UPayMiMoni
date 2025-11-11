package com.example.upaymimoni.domain.usecase.expense

import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpenseDetailUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(expenseId: String): Expense? {
        return expenseRepository.getExpenseById(expenseId).getOrNull();
    }

    /**
     * Secondary function to retrieve all expenses for a group (used by the list screen).
     * @param groupId The ID of the group.
     * @return A Flow emitting a list of Expenses.
     */
    fun getExpensesForGroup(groupId: String): Flow<List<Expense>> {
        return expenseRepository.getExpensesForGroup(groupId)
    }

}