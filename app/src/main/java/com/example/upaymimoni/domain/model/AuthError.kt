package com.example.upaymimoni.domain.model

sealed class AuthError {
    object InvalidCredentials : AuthError()
    object InvalidUser : AuthError()
    object NetworkFailure : AuthError()
    object TooManyLogins : AuthError()
    object EmailInUse : AuthError()
    data class WeakPassword(val reason: String) : AuthError()
    object InvalidEmailFormat : AuthError()
    object EmptyName : AuthError()
    object EmptyNumber : AuthError()
    object EmptyEmail : AuthError()
    object EmptyPassword : AuthError()
    data class Internal(val message: String?) : AuthError()
    data class Unknown(val message: String?) : AuthError()
}