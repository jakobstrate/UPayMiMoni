package com.example.upaymimoni.presentation.ui.auth.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.session.UserSession
import com.example.upaymimoni.domain.usecase.auth.GoogleLoginUseCase
import com.example.upaymimoni.domain.usecase.auth.LoginUseCase
import com.example.upaymimoni.presentation.ui.auth.utils.AuthUiEvent
import com.example.upaymimoni.presentation.ui.auth.utils.GoogleSignInClient
import com.example.upaymimoni.presentation.ui.auth.utils.UiMessageTranslation
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
                val message = uiMessageTranslation.getUiExceptionMessage(result.error)
                _errorMsg.value = message
            }
        }
    }

    fun onGoogleSignIn() = viewModelScope.launch {
        _loading.value = true
        val idToken = googleSignInClient.launchCredentialManager()

        if (idToken.isNullOrBlank()) {
            _loading.value = false
            _errorMsg.value =
                "Failed to retrieve google account." +
                        " Ensure you are logged in to your device and have an active internet connection"
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
                val message = uiMessageTranslation.getUiExceptionMessage(result.error)
                _errorMsg.value = message
            }
        }
    }
}