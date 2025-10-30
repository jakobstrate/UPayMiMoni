package com.example.upaymimoni.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.data.model.Expense
import com.example.upaymimoni.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseDetailViewModel(
    private val expenseRepository: ExpenseRepository,
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
            _state.value = expenseRepository.getExpenseById(expenseId)
        }
    }

}