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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseDetailViewModel(
    private val getExpenseDetailUseCase: GetExpenseDetailUseCase,
    private val expenseId: String
) : ViewModel() {
    private val _state = MutableStateFlow<Expense?>(null)
    val state: StateFlow<Expense?> = _state

    init {
        // Start collecting expenses when the ViewModel is created
        loadExpenseDetail()
    }

    /**
     * function for loading single expense
     */
    private fun loadExpenseDetail() {
        viewModelScope.launch {
            val expense = getExpenseDetailUseCase(expenseId)
            if (expense != null) {
                _state.value = expense
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

}