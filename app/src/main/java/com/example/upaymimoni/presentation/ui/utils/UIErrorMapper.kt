package com.example.upaymimoni.presentation.ui.utils

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.UpdateUserError

enum class FieldType {
    NAME,
    EMAIL,
    PHONE,
    PASSWORD,
    INPUT,
    NONE
}

data class FieldError(
    val field: FieldType = FieldType.NONE,
    val message: String
)

object UpdateUserErrorMapper {
    fun toFieldError(error: UpdateUserError): FieldError {
        return when (error) {
            UpdateUserError.EmailInUse -> FieldError(FieldType.EMAIL, "Email already in use.")
            UpdateUserError.InvalidEmail -> FieldError(FieldType.EMAIL, "Invalid email.")
            UpdateUserError.NetworkUnavailable -> FieldError(FieldType.NONE, "Network error. Please try again.")
            UpdateUserError.NotFound -> FieldError(FieldType.NONE, "An error occurred. Please try again.")
            UpdateUserError.PermissionDenied -> FieldError(FieldType.NONE, "You do not have permissions to perform this action.")
            UpdateUserError.RequiresNewLogin -> FieldError(FieldType.NONE, "Please log in again.")
            UpdateUserError.InvalidName -> FieldError(FieldType.NAME, "Invalid name")
            UpdateUserError.InvalidNumber -> FieldError(FieldType.PHONE, "Invalid mobile number")
            is UpdateUserError.Unknown -> FieldError(FieldType.NONE, "Something went wrong, try again.")
        }
    }
}

object AuthErrorMapper {
    fun toFieldError(error: AuthError): FieldError {
        return when (error) {
            AuthError.InvalidCredentials -> FieldError(FieldType.INPUT, "Incorrect Email or Password.")
            AuthError.InvalidUser -> FieldError(FieldType.INPUT, "No account with that email found.")
            AuthError.NetworkFailure -> FieldError(FieldType.NONE, "Network error, please try again.")
            AuthError.TooManyLogins -> FieldError(FieldType.NONE, "Too many attempts, try again later.")
            AuthError.EmailInUse -> FieldError(FieldType.EMAIL, "An account with that email is already in use.")
            is AuthError.WeakPassword -> FieldError(FieldType.PASSWORD, "Password too weak: ${error.reason}")
            AuthError.InvalidEmailFormat -> FieldError(FieldType.EMAIL, "Invalid email.")
            AuthError.EmptyEmail -> FieldError(FieldType.EMAIL, "Email is required.")
            AuthError.EmptyName -> FieldError(FieldType.NAME, "Name is required.")
            AuthError.EmptyNumber -> FieldError(FieldType.PHONE, "Number is required.")
            AuthError.EmptyPassword -> FieldError(FieldType.PASSWORD, "Password is required.")
            is AuthError.Internal -> FieldError(FieldType.NONE, "Something went wrong on our end, please try again.")
            is AuthError.Unknown -> FieldError(FieldType.NONE, "Something unexpected went wrong, please try again.")
        }
    }
}