package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.auth.GoogleLoginUseCase
import com.example.upaymimoni.domain.usecase.auth.LoginUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthErrorState
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.utils.GoogleSignInClient
import com.example.upaymimoni.presentation.ui.auth.utils.UiErrorType.*
import com.example.upaymimoni.presentation.ui.auth.utils.UiMessageTranslation
import com.example.upaymimoni.presentation.ui.utils.TextFieldManipulator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthLoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val uiMessageTranslation: UiMessageTranslation,
    private val userSession: UserSession,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {
    // Store TextFieldValue instead of strings, as we want the position to survive reconfigurations
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _errorState = MutableStateFlow(AuthErrorState())
    val errorState: StateFlow<AuthErrorState> = _errorState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private fun clearErrors() {
        _errorState.value = AuthErrorState()
    }

    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = TextFieldManipulator.clearWhiteSpaceFromField(newEmail)
        if (_email.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                emailError = true,
                emailMsg = "Please fill in your email."
            )
        }
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = TextFieldManipulator.clearWhiteSpaceFromField(newPass)
        if (_pass.value.text.isEmpty()) {
            _errorState.value = _errorState.value.copy(
                passwordError = true,
                passwordMsg = "Please fill in your password."
            )
        }
    }

    fun onSignInClick() = viewModelScope.launch {
        clearErrors()
        _loading.value = true
        val result = loginUseCase(_email.value.text, _pass.value.text)
        _loading.value = false

        when (result) {
            is AuthResult.Success -> {
                val user = result.data
                println("Logged in as ${user.id}; Email: ${user.email}")
                userSession.setCurrentUser(user)
                _uiEvent.emit(AuthUiEvent.NavigateToHome)
            }

            is AuthResult.Failure -> {
                val uiError = uiMessageTranslation.getUiExceptionMessage(result.error)
                when (uiError.type) {
                    EMAIL -> _errorState.value =
                        _errorState.value.copy(emailError = true, emailMsg = uiError.message)

                    PASSWORD -> _errorState.value =
                        _errorState.value.copy(passwordError = true, passwordMsg = uiError.message)

                    INPUT -> _errorState.value =
                        _errorState.value.copy(
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

    fun onGoogleSignIn() = viewModelScope.launch {
        clearErrors()
        _loading.value = true
        val idToken = googleSignInClient.launchCredentialManager()

        if (idToken.isNullOrBlank()) {
            _loading.value = false
            val message =
                "Failed to retrieve google account." +
                        " Ensure you are logged in to your device and have an active internet connection"
            _errorState.value = AuthErrorState(
                googleError = true,
                errorMsg = message
            )
            return@launch
        }

        val result = googleLoginUseCase(idToken)
        _loading.value = false

        when (result) {
            is AuthResult.Success -> {
                val user = result.data
                println("Logged in as ${user.id}; Email: ${user.email}")
                userSession.setCurrentUser(user)
                _uiEvent.emit(AuthUiEvent.NavigateToHome)
            }

            is AuthResult.Failure -> {
                val uiError = uiMessageTranslation.getUiExceptionMessage(result.error)
                _errorState.value = AuthErrorState(
                    googleError = true,
                    errorMsg = uiError.message
                )
            }
        }
    }

}