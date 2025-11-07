package com.example.upaymimoni.domain.model

class AuthException(
    val errorType: AuthErrorType,
    val errorMessage: String?
) : Exception()

enum class AuthErrorType() {
    InvalidCredentials,
    InvalidUser,
    NetworkFailure,
    TooManyLogins,
    EmailInUse,
    WeakPassword,
    InvalidEmailFormat,
    EmptyOrNull,
    InternalDataSaveError,
    Unknown
}