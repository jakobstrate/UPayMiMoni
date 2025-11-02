package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.usecase.LoginUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthLoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val uiMessageTranslation: UiMessageTranslation
) : ViewModel() {
    // Store TextFieldValue instead of strings, as we want the position to survive reconfigurations
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = newPass
    }

    fun onSignInClick() = viewModelScope.launch {
        _loading.value = true
        delay(5000)
        val result = loginUseCase(_email.value.text, _pass.value.text)
        _loading.value = false

        result.onSuccess { user ->
            println("Logged in as ${user.id}; Email: ${user.email}")
            _uiEvent.emit(AuthUiEvent.NavigateToHome)
        }.onFailure { throwable ->
            val message = uiMessageTranslation.getUiExceptionMessage(throwable)
            _errorMsg.value = message
        }
    }
}