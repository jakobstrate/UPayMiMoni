package com.example.upaymimoni.domain.model

sealed class AuthError {
    object InvalidUser : AuthError()
    object InvalidCredentials : AuthError()
    object InvalidEmailFormat : AuthError()
    object NetworkFailure : AuthError()
    object TooManyLogins : AuthError()
    object Unknown : AuthError()
}