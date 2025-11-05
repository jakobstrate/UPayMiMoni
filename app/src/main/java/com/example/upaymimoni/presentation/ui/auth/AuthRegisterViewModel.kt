package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthRegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val uiMessageTranslation: UiMessageTranslation,
    private val userSession: UserSession
) : ViewModel() {
    private val _phone = MutableStateFlow(TextFieldValue(""))
    val phone: StateFlow<TextFieldValue> = _phone
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

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
        _loading.value = true
        val result = registerUseCase(_email.value.text, _pass.value.text)
        _loading.value = false

        when (result) {
            is AuthResult.Success -> {
                val user = result.user
                println("Registered and logged in as user ${user.id}; Email: ${user.email}")
                userSession.setCurrentUser(user)
                _uiEvent.emit(AuthUiEvent.NavigateToHome)
            }

            is AuthResult.Failure -> {
                val message = uiMessageTranslation.getUiExceptionMessage(result.exception)
                _errorMsg.value = message
            }
        }
    }
}