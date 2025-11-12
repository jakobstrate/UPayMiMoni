package com.example.upaymimoni.presentation.ui.profile.utils

import com.example.upaymimoni.domain.model.UpdateUserError

// TODO: Refactor the old auth error system to use this.
enum class FieldType {
    NAME,
    EMAIL,
    PHONE,
    PASSWORD,
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