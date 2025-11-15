package com.example.upaymimoni.data.mappers

import com.example.upaymimoni.domain.model.UpdateUserError
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException

class FirebaseUpdateUserErrorMapper : ErrorMapper<Exception, UpdateUserError> {
    override fun map(error: Exception): UpdateUserError {
        return when (error) {
            is FirebaseFirestoreException -> when (error.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> UpdateUserError.PermissionDenied
                FirebaseFirestoreException.Code.UNAVAILABLE -> UpdateUserError.NetworkUnavailable
                FirebaseFirestoreException.Code.NOT_FOUND -> UpdateUserError.NotFound
                else -> UpdateUserError.Unknown(error.message)
            }

            is FirebaseAuthException -> when (error.errorCode) {
                "ERROR_INVALID_EMAIL" -> UpdateUserError.InvalidEmail
                "ERROR_EMAIL_ALREADY_IN_USE" -> UpdateUserError.EmailInUse
                "ERROR_REQUEST_RECENT_LOGIN" -> UpdateUserError.RequiresNewLogin
                else -> UpdateUserError.Unknown(error.message)
            }

            is FirebaseNetworkException -> UpdateUserError.NetworkUnavailable
            is FirebaseException -> UpdateUserError.NetworkUnavailable
            else -> UpdateUserError.Unknown(error.message)
        }
    }
}