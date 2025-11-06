package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.usecase.auth.ResetPasswordUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.utils.UiMessageTranslation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthForgotPassViewModel(
    private val passwordUseCase: ResetPasswordUseCase,
    private val uiMessageTranslation: UiMessageTranslation
) : ViewModel() {
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email
    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _showPopUp = MutableStateFlow(false)
    val showPopUp: StateFlow<Boolean> = _showPopUp

    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun removePopUp() {
        _showPopUp.value = false
    }

    fun onSendResetEmailClick() = viewModelScope.launch {
        _loading.value = true
        val result = passwordUseCase(_email.value.text)
        _loading.value = false

        when (result) {
            is AuthResult.Success -> {
                _showPopUp.value = true
            }
            is AuthResult.Failure -> {
                val message = uiMessageTranslation.getUiExceptionMessage(result.error)
                _errorMsg.value = message
            }
        }
    }
}