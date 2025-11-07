package com.example.upaymimoni.presentation.ui.auth.utils

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException

class UiMessageTranslation {
    fun getUiExceptionMessage(t: AuthException): String = when(t.errorType) {
        AuthErrorType.InvalidCredentials -> "Incorrect Email or Password"
        AuthErrorType.InvalidUser -> "No account found with that email."
        AuthErrorType.NetworkFailure -> "Network error. Please try again."
        AuthErrorType.TooManyLogins -> "Too many attempts. Try again later."
        AuthErrorType.EmailInUse -> "An account with that email is already in use"
        AuthErrorType.WeakPassword -> "Password too weak: ${t.errorMessage}"
        AuthErrorType.InvalidEmailFormat -> "Email is not a valid email."
        AuthErrorType.EmptyOrNull -> "Please fill in all required fields."
        AuthErrorType.Unknown -> "Something unexpected went wrong. Please try again."
        AuthErrorType.InternalDataSaveError -> "Something went wrong on our end. Please try again."
    }
}