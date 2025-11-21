package com.example.upaymimoni.presentation.ui.expenses.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.usecase.expense.AddExpenseUseCase
import com.example.upaymimoni.domain.usecase.expense.AddExpenseWithAttachmentUseCase
import com.example.upaymimoni.domain.usecase.expense.GetExpenseAddUIDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * single user mapping name to id for lists.
 */
data class UserUIData(
    val id: String,
    val name: String
)


data class ExpenseAddUIData(
    val groupId: String,
    val groupName: String,
    val userOptions: List<UserUIData>,
    val userIdToNameMap: Map<String, String>
)

data class AddExpenseState(
    val uiData: ExpenseAddUIData? = null,
    val name: String = "",
    val amount: String = "",
    val paidByUserId: String = "",
    val splitBetweenUserIds: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val attachmentUri: Uri? = null,
    val attachmentStatus: String = "", // should be "Uploading file" when uploading, so i know it is
    val isSliderConfirmed: Boolean = false
)

/**
 * Koin ViewModel: Manages UI state and business logic execution.
 * It exposes the immutable state (ViewState) to the UI.
 */
class ExpenseAddViewModel(
    private val addExpenseUseCase: AddExpenseWithAttachmentUseCase,
    private val getExpenseAddUIDataUseCase: GetExpenseAddUIDataUseCase,
    private val groupId: String,
    userId: String
) : ViewModel() {

    //Basic expense state and manipulation

    //make state and initialize paid by to userId from args
    private val _state = MutableStateFlow(AddExpenseState(paidByUserId = userId))
    val state: StateFlow<AddExpenseState> = _state.asStateFlow()


    init {
        loadUIData()
    }

    private fun loadUIData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = getExpenseAddUIDataUseCase(groupId)

            result.onSuccess { uiData ->

                _state.update {
                    it.copy(
                        uiData = uiData,
                        isLoading = false,
                    )
                }

            }.onFailure { e ->
                Log.e("ExpenseAdd", "Failed to load ui data of group $groupId", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load group/user list: ${e.message}"
                    )
                }
            }
        }
    }


    fun onNameChange(newName: String) {
        _state.value = state.value.copy(name = newName)
    }

    fun onAmountChange(newAmount: String) {
        _state.value = state.value.copy(amount = newAmount)
    }


    fun confirmExpense(onSuccess: () -> Unit) {
        val current = state.value
        //validation - reset confirmed if error
        if (current.name.isBlank()) {
            _state.value = current.copy(
                error = "Please enter a title of the expense.",
                isSliderConfirmed = false)
            return
        }
        if (current.amount.isBlank()) {
            _state.value = current.copy(
                error = "Please enter an amount.",
                isSliderConfirmed = false)
            return
        }
        if (current.paidByUserId.isBlank()) {
            _state.value = current.copy(
                error = "Please select who paid for the expense.",
                isSliderConfirmed = false)
            return
        }
        if (current.splitBetweenUserIds.isEmpty()) {
            _state.value = current.copy(
                error = "Please select users to split the expense with.",
                isSliderConfirmed = false)
            return
        }

        // amount conversion to double
        val amountDouble: Double
        try {
            amountDouble = current.amount.toDouble()

            if (amountDouble <= 0.0) {
                _state.value = current.copy(
                    error = "Amount must be a positive number.",
                    isSliderConfirmed = false)
                return
            }
        } catch (e: NumberFormatException) {
            _state.value = current.copy(
                error = "Amount must be a valid number: ${e.message}",
                isSliderConfirmed = false)
            return
        }

        //make object and send to repo
        viewModelScope.launch{
            _state.value = current.copy(isSaving = true)

            try {
                addExpenseUseCase(
                    expense = Expense(
                        name = current.name,
                        amount = current.amount.toDouble(),
                        payerUserId = current.paidByUserId,
                        groupId = groupId,
                        splitBetweenUserIds = current.splitBetweenUserIds,
                    ),
                    attachmentUri = current.attachmentUri,
                    onStatusUpdate = ::updateAttachmentStatus,
                )

                _state.value = state.value.copy(isSaving = false, attachmentUri = null)
                onSuccess()
            }catch (e: Exception) {
                _state.value = state.value.copy(isSaving = false)
                _state.value = state.value.copy(
                    error = "Failed to add expense: ${e.message}",
                    isSliderConfirmed = false)
            }

        }
    }

    //State for confirm slider
    fun setSliderConfirmed(confirmed: Boolean) {
        _state.value = state.value.copy(isSliderConfirmed = confirmed)
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
        _state.update { currentState ->
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
    fun saveConfirmedSplitBetweenUserIds(confirmedUsers: List<String>, paidByUserId: String) {
        _state.update { currentState ->
            currentState.copy(
                splitBetweenUserIds = confirmedUsers.filter { it != paidByUserId }
            )
        }
    }

    //File attachment


    //remove attachement
    fun removeAttachment() {
        _state.value = state.value.copy(
            attachmentUri = null,
            attachmentStatus = "Ready",
            error = null
        )
    }

    //set an attachment Uri
    fun setAttachmentUri(uri: Uri?) {
        _state.value = state.value.copy(attachmentUri = uri)
    }

    // update status, which is just status of attachment uploaded
    fun updateAttachmentStatus(status: String) {
        _state.value = state.value.copy(attachmentStatus = status)
    }



}