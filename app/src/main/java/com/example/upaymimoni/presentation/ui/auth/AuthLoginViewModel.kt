package com.example.upaymimoni.presentation.ui.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upaymimoni.data.repository.AuthException
import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthLoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    // Store TextFieldValue instead of strings, as we want the position to survive reconfigurations
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email

    private val _pass = MutableStateFlow(TextFieldValue(""))
    val pass: StateFlow<TextFieldValue> = _pass

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    fun updateEmail(newEmail: TextFieldValue) {
        _email.value = newEmail
    }

    fun updatePassword(newPass: TextFieldValue) {
        _pass.value = newPass
    }

    fun onSignInClick() = viewModelScope.launch {
        val result = loginUseCase(_email.value.text, _pass.value.text)

        result.onSuccess { user ->
            println("Logged in as ${user.email}")
        }.onFailure { throwable ->
            val message = getUiExceptionMessage(throwable)
            _errorMsg.value = message
        }
    }

    private fun getUiExceptionMessage(throwable: Throwable): String {
        val message = when (throwable) {
            is AuthException -> when (throwable.error) {
                AuthError.InvalidCredentials -> "Incorrect Email or Password."
                AuthError.InvalidEmailFormat -> "Email is not a valid email."
                AuthError.Unknown -> "Something unexpected went wrong. Please try again."
                AuthError.InvalidUser -> "No account found with that email."
                AuthError.NetworkFailure -> "Network error. Please try again."
                AuthError.TooManyLogins -> "Too many attempts. Try again later."
            }
            else -> "Unexpected Error Occurred"
        }
        return message
    }
}