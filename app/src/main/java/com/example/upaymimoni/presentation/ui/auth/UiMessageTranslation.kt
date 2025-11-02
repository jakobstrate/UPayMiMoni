package com.example.upaymimoni.presentation.ui.auth

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.AuthException

class UiMessageTranslation {
    fun getUiExceptionMessage(throwable: Throwable): String {
        val authError = (throwable as? AuthException)?.error ?: return "Unexpected error occurred"
        return when (authError) {
            is AuthError.InvalidCredentials -> "Incorrect Email or Password"
            is AuthError.InvalidEmailFormat -> "Email is not a valid email."
            is AuthError.Unknown -> "Something unexpected went wrong. Please try again."
            is AuthError.InvalidUser -> "No account found with that email."
            is AuthError.NetworkFailure -> "Network error. Please try again."
            is AuthError.TooManyLogins -> "Too many attempts. Try again later."
            is AuthError.EmptyOrNull -> "Please fill in all required fields."
            is AuthError.EmailInUse -> "An account with that email is already in use"
            is AuthError.WeakPassword -> "Password too weak: ${authError.message}"
        }
    }
}