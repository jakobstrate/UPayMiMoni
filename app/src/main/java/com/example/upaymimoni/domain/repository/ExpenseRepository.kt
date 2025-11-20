package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for interacting with Expense data sources.
 */
interface ExpenseRepository {
    /**
     * Adds a new expense to the data source.
     * @param expense The Expense object to be saved.
     * @return A flow emitting the result (Success or Error).
     */
    suspend fun addExpense(expense: Expense): Result<Unit>

    /**
     * Retrieves all expenses for a given group in real-time.
     * @param groupId The ID of the group whose expenses to fetch.
     * @return A flow emitting a list of Expenses.
     */
    fun getExpensesForGroup(groupId: String): Flow<List<Expense>>

    /**
     * Retrieves all expenses for a given user in a specific group.
     * @param userId The ID of the user whose expenses to fetch.
     * @param groupId The ID of the group whose expenses to fetch.
     * @return A flow emitting a list of Expenses.
     */
    fun getExpensesOfUserForGroup(userId: String,groupId: String): Flow<List<Expense>>

    /**
     * retrieves a single expense by id
     * @param id The id of the expense
     */
    suspend fun getExpenseById(id: String) : Result<Expense>
    /**
     * Removes an expense document from the data source by its ID.
     * @param expenseId The unique ID of the expense to be removed.
     * @return A Result object indicating success or failure.
     */
    suspend fun removeExpense(expenseId: String): Result<Unit>
}