package com.example.upaymimoni.domain.model

class AuthException(val error: AuthError) : Exception(error.message)
sealed class AuthError(val message: String) {
    data object InvalidUser : AuthError("Incorrect Email or Password.")
    data object InvalidCredentials : AuthError("Incorrect Email or Password.")
    data object InvalidEmailFormat : AuthError("Incorrect Email or Password.")
    data object NetworkFailure : AuthError("Network error. Please try again.")
    data object TooManyLogins : AuthError("Too many login attempts. Try again later.")
    data object EmptyOrNull : AuthError("Please fill in all required fields.")
    data object EmailInUse : AuthError("An account with that email is already in use")
    data class WeakPassword(val reason: String) : AuthError(reason)
    data object Unknown : AuthError("Unexpected Error Occurred")
}