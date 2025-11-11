package com.example.upaymimoni.presentation.ui.expenses.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Attachment
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.AttachmentType
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.usecase.expense.AddExpenseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class AddExpenseState(
    val name: String = "",
    val amount: String = "",
    val paidByUserId: String = "",
    val splitBetweenUserIds: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val error: String? = null,
    val attachment: Attachment? = null
)

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseAddViewModel(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val groupId: String,
    userId: String
) : ViewModel() {
    //Basic expense state and manipulation

    //make state and initialize paid by to userId from args
    val state = MutableStateFlow(AddExpenseState(paidByUserId = userId))


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
            val result = addExpenseUseCase(
                name = current.name,
                amount = current.amount.toDouble(),
                payerUserId = current.paidByUserId,
                groupId = groupId,
                splitBetweenUserIds = current.splitBetweenUserIds,
                attachment = current.attachment
            )

            if (result.isSuccess) {
                onSuccess()
            }
        }
    }

    //State for popups

    // State for Single-select paioBy popup
    private val _showPaidByPopup = MutableStateFlow(false)
    val showPaidByPopup: StateFlow<Boolean> = _showPaidByPopup.asStateFlow()

    fun openPaidByPopup() {
        _showPaidByPopup.value = true
    }

    fun closePaidByPopup() {
        _showPaidByPopup.value = false
    }

    // Function to update the paidBy user
    fun updatePaidByUserId(selectedUserId: String) {
        state.update { currentState ->
            currentState.copy(
                paidByUserId = selectedUserId
            )
        }
        println(state.value.paidByUserId)
    }

    // State for Multi-Select Split Between Dialog
    private val _showSplitBetweenPopup = MutableStateFlow(false)
    val showSplitBetweenPopup: StateFlow<Boolean> = _showSplitBetweenPopup.asStateFlow()

    // Temp state for pending confirmation selections
    private val _pendingSelectedUserIds = MutableStateFlow<List<String>>(emptyList())
    val pendingSelectedUserIds: StateFlow<List<String>> = _pendingSelectedUserIds.asStateFlow()

    fun openSpltBetweenPopup() {
        // initialize with already saved state
        _pendingSelectedUserIds.value = state.value.splitBetweenUserIds
        _showSplitBetweenPopup.value = true
    }

    fun closeSpltBetweenPopup() {
        _showSplitBetweenPopup.value = false
    }


    // Toggles the selection of a user in the temp set.
    fun toggleUserSelection(userId: String) {
        _pendingSelectedUserIds.update { currentSet ->
            if (currentSet.contains(userId)) {
                // Remove user
                currentSet - userId
            } else {
                // Add user
                currentSet + userId
            }
        }
    }

    // Function to update the paidBy user
    fun saveConfirmedSplitBetweenUserIds(confirmedUsers: List<String>) {
        state.update { currentState ->
            currentState.copy(
                splitBetweenUserIds = confirmedUsers
            )
        }
    }

    //File attachment

    private val _showFileAttachmentChooser = MutableStateFlow(false)
    val showFileAttachmentChooser: StateFlow<Boolean> = _showFileAttachmentChooser.asStateFlow()

    fun openFileAttachmentChooser() {
        _showFileAttachmentChooser.value = true
    }

    fun fileChooserLaunched() {
        _showFileAttachmentChooser.value = false
    }

    //TODO final implementation and check type of file
    fun onAttachmentSelected(uri: Uri?) {
        if (uri != null) {
            val newAttachment = Attachment(attachmentUrl = uri.toString(), type = AttachmentType.RECEIPT)
            state.update { it.copy(attachment = newAttachment) }
        }
    }



}