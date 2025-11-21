package com.example.upaymimoni.presentation.ui.groups.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.model.factory.NotificationFactory
import com.example.upaymimoni.domain.model.result.NotificationResult
import com.example.upaymimoni.domain.service.ToastService
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.expense.GetExpenseDetailUseCase
import com.example.upaymimoni.domain.usecase.groups.CalculateSettlementUseCase
import com.example.upaymimoni.domain.usecase.notification.SendNotificationToUserUseCase
import com.example.upaymimoni.domain.usecase.user.GetUserUseCase
import com.example.upaymimoni.presentation.ui.groups.components.BannerInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettledGroupViewModel(
    private val userSession: UserSession,
    private val notificationToUserUseCase: SendNotificationToUserUseCase,
    private val toastService: ToastService,
    private val calculateSettlementUseCase: CalculateSettlementUseCase,
    private val getExpenseUseCase: GetExpenseDetailUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    val currentUser: StateFlow<User?> = userSession.currentUser

    private val _personalBalance = MutableStateFlow(0.0)
    val personalBalance: StateFlow<Double> = _personalBalance

    private val _totalExpenditure = MutableStateFlow(0.0)
    val totalExpenditure: StateFlow<Double> = _totalExpenditure

    private val _debtorList = MutableStateFlow<List<BannerInformation>>(emptyList())
    val debtorList: StateFlow<List<BannerInformation>> = _debtorList

    private val _creditorList = MutableStateFlow<List<BannerInformation>>(emptyList())
    val creditorList: StateFlow<List<BannerInformation>> = _creditorList

    fun populateSettlement(group: Group) = viewModelScope.launch {
        val user = currentUser.value ?: return@launch
        val expenseIds = group.expenses
        val expenses = mutableListOf<Expense>()

        if (expenseIds.isNullOrEmpty()) {
            return@launch
        }

        expenseIds.forEach { expenseId ->
            val result = getExpenseUseCase(expenseId)
            result.fold(
                onSuccess = {
                    expenses.add(it)
                },
                onFailure = {
                    return@launch
                }
            )
        }

        val balance = calculateSettlementUseCase.calculateBalances(expenses)

        setPersonalBalanceFromBalance(balance, user.id)
        setTotalExpenditureFromExpenses(expenses)
        setDebtorAndCreditorLists(balance, user.id)
    }


    private fun setPersonalBalanceFromBalance(
        balance: Map<String, Double>,
        currentUserId: String
    ) {
        balance[currentUserId]?.let { amount ->
            _personalBalance.value = amount
        }
    }

    private fun setTotalExpenditureFromExpenses(expenses: List<Expense>) {
        var sum = 0.0
        expenses.forEach { expense ->
            sum += expense.amount
        }

        _totalExpenditure.value = sum
    }

    private suspend fun setDebtorAndCreditorLists(
        balance: Map<String, Double>,
        currentUserId: String
    ) {
        val transactions = calculateSettlementUseCase.minimizeTransactions(balance)
        val localCreditor = mutableListOf<BannerInformation>()
        val localDebtor = mutableListOf<BannerInformation>()

        transactions.forEach { t ->
            if (t.toUserId == currentUserId) {
                val userResult = getUserUseCase(t.fromUserId)
                userResult.fold(
                    onSuccess = {
                        val information = BannerInformation(
                            profilePictureUrl = it.profilePictureUrl ?: "",
                            name = it.displayName ?: "",
                            phoneNumber = it.phoneNumber ?: "",
                            amount = t.amount,
                            userId = it.id
                        )
                        localDebtor.add(information)
                    },
                    onFailure = {
                        error("Failed to get users")
                    }
                )
            } else if (t.fromUserId == currentUserId) {
                val userResult = getUserUseCase(t.toUserId)
                userResult.fold(
                    onSuccess = {
                        val information = BannerInformation(
                            profilePictureUrl = it.profilePictureUrl ?: "",
                            name = it.displayName ?: "",
                            phoneNumber = it.phoneNumber ?: "",
                            amount = t.amount,
                            userId = it.id
                        )
                        localCreditor.add(information)
                    },
                    onFailure = {
                        error("Failed to get users")
                    }
                )
            }
        }

        _debtorList.value = localDebtor
        _creditorList.value = localCreditor
    }

    fun onNotifyClick(
        userId: String,
        amount: Double,
        currencyString: String
    ) = viewModelScope.launch {
        val user = currentUser.value ?: return@launch

        val result = notificationToUserUseCase(
            userId = userId,
            notificationBody = NotificationFactory.paymentRequestNotification(
                senderName = user.displayName ?: "A Person",
                amount = "${"%.2f".format(amount)} $currencyString"
            )
        )

        when (result) {
            is NotificationResult.Success -> {
                toastService.showToast("Notification delivered.")
            }

            else -> {
                toastService.showToast("Failed to send notification.")
            }
        }
    }
}