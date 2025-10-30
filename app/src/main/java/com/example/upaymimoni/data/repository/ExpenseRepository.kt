package com.example.upaymimoni.data.repository

import com.example.upaymimoni.data.model.Attachment
import com.example.upaymimoni.data.model.AttachmentType
import com.example.upaymimoni.data.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    fun getExpenseById(id: String) : Expense?
}

// Simple mock implementation for demonstration purposes (Koin would bind the interface to this)
class MockExpenseRepository : ExpenseRepository {
    private val mockExpenses = mutableListOf<Expense>()

    // Initialize with some mock data for a group so the UI isn't empty on launch
    init {
        val mockUserId = "mock-user-123"
        val mockGroupId = "weshare-trip-001"
        val mockAttachment: Attachment = Attachment(
            "1","://attachments/receipt.pdf", AttachmentType.RECEIPT)
        mockExpenses.add(Expense(
            id = "mock1",
            name = "Flight Tickets",
            amount = 550.00,
            date = System.currentTimeMillis() - 86400000 * 2,
            payerUserId = mockUserId,
            groupId = mockGroupId,
            attachment = mockAttachment
        ))
        mockExpenses.add(Expense(
            id = "mock2",
            name = "Dinner at Italian Place",
            amount = 75.50,
            date = System.currentTimeMillis(),
            payerUserId = mockUserId,
            groupId = mockGroupId,
            attachment = null
        ))
    }

    override suspend fun addExpense(expense: Expense): Result<Unit> {
        // Simulate database latency
        kotlinx.coroutines.delay(500)

        // Assign a mock ID
        val expenseWithId = expense.copy(id = java.util.UUID.randomUUID().toString())
        mockExpenses.add(expenseWithId)
        println("Expense added: ${expenseWithId.name} to Group: ${expenseWithId.groupId}")
        return Result.success(Unit)
    }

    // Note: A real implementation would use Firestore's snapshot listener here.
    override fun getExpensesForGroup(groupId: String): Flow<List<Expense>> {
        return flow {
            // Simulate a simple initial load and subsequent updates
            // filtering by groupId
            emit(mockExpenses.filter { it.groupId == groupId }
                .sortedByDescending { it.createdAt })

        }
    }

    override fun getExpensesOfUserForGroup(
        userId: String,
        groupId: String
    ): Flow<List<Expense>> {
        return flow {
            // Simulate a simple initial load and subsequent updates
            // filtering by groupId and user id
            emit(mockExpenses.filter { it.groupId == groupId && it.payerUserId == userId }
                .sortedByDescending { it.createdAt })

        }
    }

    override fun getExpenseById(id: String): Expense? {
        return mockExpenses.find{it.id == id}
    }
}
