package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.usecase.auth.RegisterUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.utils.AuthErrorMapper
import com.example.upaymimoni.presentation.ui.utils.FieldType
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthRegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    private val _name = MutableStateFlow(TextFieldValue(""))
    val name: StateFlow<TextFieldValue> = _name
    private val _number = MutableStateFlow(TextFieldValue(""))
    val number: StateFlow<TextFieldValue> = _number
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorState = MutableStateFlow(AuthErrorState())
    val errorState: StateFlow<AuthErrorState> = _errorState

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun updateName(newName: TextFieldValue) {
        _name.value = newName

        if (_name.value.text.isEmpty()) {
            setFieldError(FieldType.NAME, "Please fill in your name.")
        } else {
            clearFieldError(FieldType.NAME)
        }
    }

    fun updatePhone(newPhone: TextFieldValue) {
        val cleaned = TextFieldManipulator.removeAllNonNumerics(newPhone)
        _number.value = cleaned
        if (cleaned.text.isEmpty()) {
            setFieldError(FieldType.PHONE, "Please fill in your phone number.")
        } else {
            clearFieldError(FieldType.PHONE)
        }
    }

    fun updateEmail(newEmail: TextFieldValue) {
        val cleaned = TextFieldManipulator.clearWhiteSpaceFromField(newEmail)
        _email.value = cleaned
        if (cleaned.text.isEmpty()) {
            setFieldError(FieldType.EMAIL, "Please fill in your email.")
        } else {
            clearFieldError(FieldType.EMAIL)
        }
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = newPass
        if (_pass.value.text.isEmpty()) {
            setFieldError(FieldType.PASSWORD, "Please fill in your password.")
        } else {
            clearFieldError(FieldType.PASSWORD)
        }
    }

    fun onRegisterClick() = viewModelScope.launch {
        clearErrors()
        _loading.value = true

        val result = registerUseCase(
            _name.value.text,
            _number.value.text,
            _email.value.text,
            _pass.value.text
        )
        _loading.value = false

        when (result) {
            is AuthResult.Success -> handleSuccessfulRegister()
            is AuthResult.Failure -> handleAuthFailure(result.error)
        }
    }

    private suspend fun handleSuccessfulRegister() {
        _uiEvent.emit(AuthUiEvent.NavigateToHome)
    }

    private fun handleAuthFailure(error: AuthError) {
        val fieldError = AuthErrorMapper.toFieldError(error)
        when (fieldError.field) {
            FieldType.NAME -> setFieldError(FieldType.NAME, fieldError.message)
            FieldType.PHONE -> setFieldError(FieldType.PHONE, fieldError.message)
            FieldType.EMAIL -> setFieldError(FieldType.EMAIL, fieldError.message)
            FieldType.PASSWORD -> setFieldError(FieldType.PASSWORD, fieldError.message)
            FieldType.INPUT -> setFieldError(FieldType.INPUT, fieldError.message)
            FieldType.NONE -> setGeneralError(fieldError.message)
            else -> Unit
        }
    }

    private fun clearErrors() {
        _errorState.value = AuthErrorState()
    }

    private fun setFieldError(type: FieldType, message: String) {
        _errorState.value = when (type) {
            FieldType.NAME -> _errorState.value.copy(
                nameError = true,
                nameMsg = message
            )

            FieldType.PHONE -> _errorState.value.copy(
                numberError = true,
                numberMsg = message
            )

            FieldType.EMAIL -> _errorState.value.copy(
                emailError = true,
                emailMsg = message
            )

            FieldType.PASSWORD -> _errorState.value.copy(
                passwordError = true,
                passwordMsg = message
            )

            FieldType.INPUT -> _errorState.value.copy(
                nameError = true,
                numberError = true,
                emailError = true,
                passwordError = true,
                emailMsg = message,
                passwordMsg = message,
                nameMsg = message,
                numberMsg = message,
            )
            // We handle a state that we do not care about, by simply ignoring it. (Best practice)
            else -> _errorState.value
        }
    }

    private fun clearFieldError(type: FieldType) {
        _errorState.value = when (type) {
            FieldType.NAME -> _errorState.value.copy(nameError = false, nameMsg = null)
            FieldType.PHONE -> _errorState.value.copy(numberError = false, numberMsg = null)
            FieldType.EMAIL -> _errorState.value.copy(emailError = false, emailMsg = null)
            FieldType.PASSWORD -> _errorState.value.copy(passwordError = false, passwordMsg = null)
            else -> _errorState.value
        }
    }

    private fun setGeneralError(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(AuthUiEvent.ShowSnackbar(message))
        }
        _errorState.value = _errorState.value.copy(errorMsg = null)
    }
}