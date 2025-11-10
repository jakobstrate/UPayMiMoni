package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.auth.RegisterUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.utils.UiErrorType.*
import com.example.upaymimoni.presentation.ui.auth.utils.UiMessageTranslation
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
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

    private fun clearErrors() {
        _errorState.value = AuthErrorState()
    }

    fun updateName(newName: TextFieldValue) {
        _name.value = newName
        _errorState.value = _errorState.value.copy(nameError = false, nameMsg = null)

        if (_name.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                nameError = true,
                nameMsg = "Please fill in your name."
            )
        }
    }

    fun updatePhone(newPhone: TextFieldValue) {
        _number.value = TextFieldManipulator.removeAllNonNumerics(newPhone)
        _errorState.value = _errorState.value.copy(numberError = false, numberMsg = null)

        if (_number.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                numberError = true,
                numberMsg = "Please fill in your phone number."
            )
        }
    }

    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = TextFieldManipulator.clearWhiteSpaceFromField(newEmail)
        _errorState.value = _errorState.value.copy(emailError = false, emailMsg = null)

        if (_email.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                emailError = true,
                emailMsg = "Please fill in your email."
            )
        }
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = TextFieldManipulator.clearWhiteSpaceFromField(newPass)
        _errorState.value = _errorState.value.copy(passwordError = false, passwordMsg = null)

        if (_pass.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                passwordError = true,
                passwordMsg = "Please fill in your password."
            )
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
            is AuthResult.Success -> {
                val user = result.data
                println("Registered and logged in as user ${user.id}; Email: ${user.email}")
                userSession.setCurrentUser(user)
                _uiEvent.emit(AuthUiEvent.NavigateToHome)
            }

            is AuthResult.Failure -> {
                val uiError = uiMessageTranslation.getUiExceptionMessage(result.error)
                when (uiError.type) {
                    NAME -> _errorState.value =
                        _errorState.value.copy(nameError = true, nameMsg = uiError.message)

                    NUMBER -> _errorState.value =
                        _errorState.value.copy(numberError = true, numberMsg = uiError.message)

                    EMAIL -> _errorState.value =
                        _errorState.value.copy(emailError = true, emailMsg = uiError.message)

                    PASSWORD -> _errorState.value =
                        _errorState.value.copy(passwordError = true, passwordMsg = uiError.message)

                    INPUT -> _errorState.value =
                        _errorState.value.copy(
                            nameError = true,
                            nameMsg = uiError.message,
                            numberError = true,
                            numberMsg = uiError.message,
                            emailError = true,
                            passwordError = true,
                            emailMsg = uiError.message,
                            passwordMsg = uiError.message
                        )

                    GOOGLE -> _errorState.value =
                        _errorState.value.copy(googleError = true, errorMsg = uiError.message)

                    else -> _errorState.value = _errorState.value.copy()
                }
            }
        }
    }
}