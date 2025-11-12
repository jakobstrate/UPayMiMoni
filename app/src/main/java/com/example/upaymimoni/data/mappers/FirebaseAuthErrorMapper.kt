package com.example.upaymimoni.data.mappers

import com.example.upaymimoni.domain.model.AuthError
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FirebaseAuthErrorMapper : ErrorMapper<Exception, AuthError> {
    override fun map(error: Exception): AuthError {
        return when (error) {
            is FirebaseAuthInvalidUserException -> AuthError.InvalidUser
            is FirebaseNetworkException -> AuthError.NetworkFailure
            is FirebaseTooManyRequestsException -> AuthError.TooManyLogins
            is FirebaseAuthUserCollisionException -> AuthError.EmailInUse
            is FirebaseAuthWeakPasswordException -> AuthError.WeakPassword(
                error.reason ?: "Password is too weak, no specific reason reported."
            )
            is FirebaseAuthInvalidCredentialsException -> when (error.errorCode) {
                "ERROR_WRONG_PASSWORD" -> AuthError.InvalidCredentials
                "ERROR_INVALID_EMAIL" -> AuthError.InvalidEmailFormat
                else -> AuthError.Unknown(error.message)
            }

            else -> AuthError.Unknown(error.message)
        }
    }
}