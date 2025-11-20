package com.example.upaymimoni.presentation.ui.expenses.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.usecase.expense.GetExpenseDetailUseCase
import com.example.upaymimoni.domain.usecase.expense.RemoveExpenseUseCase
import com.example.upaymimoni.domain.usecase.groups.GetGroupUseCase
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ExpenseDetailState(
    val expense: Expense? = null,
    val expenseUI: ExpenseDetailUIData? = null,
    val isLoading: Boolean = true,
    val isDeleting: Boolean = false,
    val error: String? = null
)

data class ExpenseDetailUIData(
    val payerUserName: String,
    val groupName: String,
    val expenseName: String,
    val amount: Double,
    val attachmentUrl: String?,
    val createdAt: Long
)

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseDetailViewModel(
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val removeExpenseUseCase: RemoveExpenseUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    private val expenseId: String
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseDetailState())
    val state: StateFlow<ExpenseDetailState> = _state.asStateFlow()

    init {
        // Start collecting expenses when the ViewModel is created
        loadExpenseDetail()

    }

    /**
     * function for loading single expense and ui data
     */
    private fun loadExpenseDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getExpenseDetailUseCase(expenseId)

            result.onSuccess { expense ->
                val uiData = loadExpenseUIData(expense)

                _state.update { currentState ->
                    currentState.copy(
                        expense = expense,
                        expenseUI = uiData,
                        isLoading = false
                    )
                }
                _state.update { it.copy(expense = expense, isLoading = false) }
            }.onFailure { e ->
                Log.e("ExpenseDetail", "Failed to load expense $expenseId", e)
                _state.update { it.copy(isLoading = false, error = "Failed to load expense: ${e.message}") }
            }
        }
    }

    /**
     * function for loading single expense ui data like group name and username
     */
    private suspend fun loadExpenseUIData(expense: Expense) : ExpenseDetailUIData {
        return coroutineScope {
            val userAsynced = async { getUserUseCase(expense.payerUserId) }
            val groupAsynced = async { getGroupUseCase(expense.groupId) }

            val userResult = userAsynced.await()
            val groupResult = groupAsynced.await()

            val payerUserName = userResult.getOrNull()?.displayName
                ?: "Unknown User (ID: ${expense.payerUserId})"

            val groupName = groupResult.getOrNull()?.groupName
                ?: "Unknown Group (ID: ${expense.groupId})"

            ExpenseDetailUIData(
                payerUserName = payerUserName,
                groupName = groupName,
                expenseName = expense.name,
                amount = expense.amount,
                attachmentUrl = expense.attachmentUrl,
                createdAt = expense.createdAt,
            )
        }
    }

    /**
     * Deletes the current expense and calls a success callback.
     */
    fun deleteExpense(onSuccess: () -> Unit) {
        val expense = _state.value.expense
        if (expense == null) {
            _state.update { it.copy(error = "Cannot delete: Expense is null.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, error = null) }

            val result = removeExpenseUseCase(expense.id)

            result.onSuccess {
                onSuccess()
            }.onFailure { e ->
                Log.e("ExpenseDetail", "Failed to delete expense ${expense.id}", e)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        error = "Deletion failed: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * function to open the attachment URL using an Android Intent.
     * This launches the device's default app for the file type.
     */
    fun openAttachmentUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Open Attachment with..."))
        } catch (e: Exception) {
            Log.e("ExpenseDetailScreen", "Failed to open attachment URL: $url", e)
        }
    }

    //popups

    private val _showDeleteConfirmation = MutableStateFlow(false)
    val showDeleteConfirmation: StateFlow<Boolean> = _showDeleteConfirmation.asStateFlow()

    fun openShowDeleteConfirmation() {
        _showDeleteConfirmation.value = true
        println("TEST")
    }

    fun closeShowDeleteConfirmation() {
        _showDeleteConfirmation.value = false
    }

}