package com.example.upaymimoni.presentation.ui.auth.utils

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException

data class UiError(
    val message: String,
    val type: UiErrorType
)

enum class UiErrorType {
    NAME,
    NUMBER,
    EMAIL,
    PASSWORD,
    INPUT,
    GOOGLE,
    GENERAL
}

class UiMessageTranslation {
    /**
     * Maps the different AuthErrorType's to a UiError that can be displayed on screen.
     */
    fun getUiExceptionMessage(t: AuthException): UiError = when (t.errorType) {
        AuthErrorType.InvalidCredentials -> UiError(
            message = "Incorrect Email or Password.",
            type = UiErrorType.INPUT
        )

        AuthErrorType.InvalidUser -> UiError(
            message = "No account with that email found.",
            type = UiErrorType.INPUT
        )

        AuthErrorType.NetworkFailure -> UiError(
            message = "Network error. Please try again.",
            type = UiErrorType.GENERAL
        )

        AuthErrorType.TooManyLogins -> UiError(
            message = "Too many attempts. Try again later.",
            type = UiErrorType.GENERAL
        )

        AuthErrorType.EmailInUse -> UiError(
            message = "An account with that email is already in use.",
            type = UiErrorType.EMAIL
        )

        AuthErrorType.WeakPassword -> UiError(
            message = "Password too weak: ${t.errorMessage}",
            type = UiErrorType.PASSWORD
        )

        AuthErrorType.InvalidEmailFormat -> UiError(
            message = "Email is not a valid email.",
            type = UiErrorType.EMAIL
        )

        AuthErrorType.Unknown -> UiError(
            message = "Something unexpected went wrong. Please try again.",
            type = UiErrorType.GENERAL
        )

        AuthErrorType.InternalDataSaveError -> UiError(
            message = "Something went wrong on our end. Please try again.",
            type = UiErrorType.GENERAL
        )

        AuthErrorType.EmptyName -> UiError(
            message = "Name is required.",
            type = UiErrorType.NAME
        )

        AuthErrorType.EmptyNumber -> UiError(
            message = "Number is required.",
            type = UiErrorType.NUMBER
        )

        AuthErrorType.EmptyEmail -> UiError(
            message = "Email is required.",
            type = UiErrorType.EMAIL
        )

        AuthErrorType.EmptyPassword -> UiError(
            message = "Password is required.",
            type = UiErrorType.PASSWORD
        )
    }
}