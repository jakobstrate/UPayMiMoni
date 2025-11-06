package com.example.upaymimoni.domain.usecase

import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow


class GetExpenseDetailUseCase(private val expenseRepository: ExpenseRepository) {
    operator fun invoke(expenseId: String): Expense? {
        // The Use Case delegates the responsibility of fetching the data to the repository.
        // It returns a Flow to enable reactive/real-time updates if the underlying data changes.
        return expenseRepository.getExpenseById(expenseId)
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