package com.example.upaymimoni.presentation.ui.auth

import com.example.upaymimoni.data.repository.AuthException
import com.example.upaymimoni.domain.model.AuthError

class UiMessageTranslation {
    fun getUiExceptionMessage(throwable: Throwable): String {
        val message = when (throwable) {
            is AuthException -> when (throwable.error) {
                AuthError.InvalidCredentials -> "Incorrect Email or Password."
                AuthError.InvalidEmailFormat -> "Email is not a valid email."
                AuthError.Unknown -> "Something unexpected went wrong. Please try again."
                AuthError.InvalidUser -> "No account found with that email."
                AuthError.NetworkFailure -> "Network error. Please try again."
                AuthError.TooManyLogins -> "Too many attempts. Try again later."
                AuthError.EmptyOrNull -> "Please fill in all required fields."
                AuthError.EmailInUse -> "An account with that email is already in use"
            }
            else -> "Unexpected Error Occurred"
        }
        return message
    }
}