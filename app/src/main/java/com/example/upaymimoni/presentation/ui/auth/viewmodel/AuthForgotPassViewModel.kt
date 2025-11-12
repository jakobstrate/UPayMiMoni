package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.usecase.auth.ResetPasswordUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.utils.AuthErrorMapper
import com.example.upaymimoni.presentation.ui.utils.FieldType
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthForgotPassViewModel(
    private val passwordUseCase: ResetPasswordUseCase,
) : ViewModel() {
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email
    private val _errorState = MutableStateFlow(AuthErrorState())
    val errorState: StateFlow<AuthErrorState> = _errorState
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _showPopUp = MutableStateFlow(false)
    val showPopUp: StateFlow<Boolean> = _showPopUp

    fun updateEmail(newEmail: TextFieldValue) {
        val cleaned = TextFieldManipulator.clearWhiteSpaceFromField(newEmail)
        _email.value = cleaned

        if (cleaned.text.isEmpty()) {
            setFieldError(FieldType.EMAIL, "Please fill in your email.")
        } else {
            clearFieldError(FieldType.EMAIL)
        }
    }

    fun removePopUp() {
        _showPopUp.value = false
    }

    fun onSendResetEmailClick() = viewModelScope.launch {
        clearErrors()
        _loading.value = true

        val result = passwordUseCase(_email.value.text)
        _loading.value = false

        when (result) {
            is AuthResult.Success -> handleSuccessfulReset()
            is AuthResult.Failure -> handleAuthFailure(result.error)
        }
    }

    private fun clearErrors() {
        _errorState.value = AuthErrorState()
    }

    private fun handleSuccessfulReset() {
        _showPopUp.value = true
    }

    private fun handleAuthFailure(error: AuthError) {
        val fieldError = AuthErrorMapper.toFieldError(error)
        when (fieldError.field) {
            FieldType.EMAIL -> setFieldError(FieldType.EMAIL, fieldError.message)
            FieldType.INPUT -> setFieldError(FieldType.INPUT, fieldError.message)
            else -> Unit
        }
    }

    private fun setFieldError(type: FieldType, message: String) {
        _errorState.value = when (type) {
            FieldType.EMAIL -> _errorState.value.copy(
                emailError = true,
                emailMsg = message
            )
            FieldType.INPUT -> _errorState.value.copy(
                emailError = true,
                emailMsg = message
            )
            else -> _errorState.value
        }
    }

    private fun clearFieldError(type: FieldType) {
        _errorState.value = when (type) {
            FieldType.EMAIL -> _errorState.value.copy(
                emailError = false,
                emailMsg = null
            )
            else -> _errorState.value
        }
    }
}