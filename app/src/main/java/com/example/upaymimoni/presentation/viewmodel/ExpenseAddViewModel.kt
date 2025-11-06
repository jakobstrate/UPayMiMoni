package com.example.upaymimoni.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Attachment
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class AddExpenseState(
    val name: String = "",
    val amount: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val attachment: Attachment? = null
)

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseAddViewModel(
    private val expenseRepository: ExpenseRepository,
    private val groupId: String,
    private val userId: String
) : ViewModel() {
    val state = MutableStateFlow(AddExpenseState())

    fun onNameChange(newName: String) {
        state.value = state.value.copy(name = newName)
    }

    fun onAmountChange(newAmount: String) {
        state.value = state.value.copy(amount = newAmount)
    }

    fun addAttachment(newAttachment: Attachment) {
        state.value = state.value.copy(attachment = newAttachment)
    }

    fun confirmExpense(onSuccess: () -> Unit) {
        val current = state.value
        //validation
        if (current.name.isBlank() || current.amount.isBlank()){
            state.value = current.copy(error = "no name or amount on expense")
        }
        //make object and send to repo
        viewModelScope.launch{
            state.value = current.copy(isSaving = true)

            val expense = Expense(
                id = UUID.randomUUID().toString(),
                name = current.name,
                amount = current.amount.toDouble(),
                payerUserId = userId,
                groupId = groupId,
                attachment = current.attachment,
            )

            expenseRepository.addExpense(expense)

            onSuccess()
        }
    }


}