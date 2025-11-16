package com.example.upaymimoni.data.repository

import android.net.Uri
import coil3.toUri
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.repository.ExpenseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID


// Simple mock implementation for demonstration purposes (Koin would bind the interface to this)
class MockExpenseRepository : ExpenseRepository {
    private val mockExpenses = mutableListOf<Expense>()

    // Initialize with some mock data for a group so the UI isn't empty on launch
    init {
        val mockUserId = "mock-user-123"
        val mockGroupId = "weshare-trip-001"
        val mockAttachment = "http//example.test.test/atachment"
        mockExpenses.add(Expense(
            id = "mock1",
            name = "Flight Tickets",
            amount = 550.00,
            payerUserId = mockUserId,
            groupId = mockGroupId,
            attachmentUrl = mockAttachment
        ))
        mockExpenses.add(Expense(
            id = "mock2",
            name = "Dinner at Italian Place",
            amount = 75.50,
            payerUserId = mockUserId,
            groupId = mockGroupId,
            attachmentUrl = null
        ))
    }

    override suspend fun addExpense(expense: Expense): Result<Unit> {
        // Simulate database latency
        delay(500)

        // Assign a mock ID
        val expenseWithId = expense.copy(id = UUID.randomUUID().toString())
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

    override suspend fun getExpenseById(id: String): Result<Expense> {
        val expense = mockExpenses.find{it.id == id}
        if (expense != null) {
            return Result.success(expense)
        }
        return Result.failure(NoSuchElementException())
    }
}
