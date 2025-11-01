package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthRegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val uiMessageTranslation: UiMessageTranslation
) : ViewModel() {
    private val _phone = MutableStateFlow(TextFieldValue(""))
    val phone: StateFlow<TextFieldValue> = _phone
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun updatePhone(newPhone: TextFieldValue) {
        _phone.value = newPhone
    }

    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = newPass
    }

    fun onRegisterClick() = viewModelScope.launch {
        val result = registerUseCase(_email.value.text, _pass.value.text)

        result.onSuccess { user ->
            println("Registered and logged in as user ${user.id}; Email: ${user.email}")
            _uiEvent.emit(AuthUiEvent.NavigateToHome)
        }.onFailure { throwable ->
            val message = uiMessageTranslation.getUiExceptionMessage(throwable)
            _errorMsg.value = message
        }
    }
}