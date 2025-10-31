package com.example.upaymimoni.data.repository

import com.example.upaymimoni.domain.model.AuthError
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("Auth returned null user")

            Result.success(
                User(
                    id = firebaseUser.uid,
                    email = firebaseUser.email
                )
            )
        } catch (e: Exception) {
            val error = mapExceptionToAuthError(e)
            Result.failure(AuthException(error))
        }
    }

    override suspend fun registerUser(email: String, password: String) {
        TODO("Not yet implemented")
    }

    private fun mapExceptionToAuthError(e: Exception): AuthError {
        return when (e) {
            is FirebaseAuthInvalidUserException -> AuthError.InvalidUser
            is FirebaseAuthInvalidCredentialsException -> when (e.errorCode) {
                "ERROR_WRONG_PASSWORD" -> AuthError.InvalidCredentials
                "ERROR_INVALID_EMAIL" -> AuthError.InvalidEmailFormat
                else -> AuthError.Unknown
            }
            is FirebaseNetworkException -> AuthError.NetworkFailure
            is FirebaseTooManyRequestsException -> AuthError.TooManyLogins
            else -> AuthError.Unknown
        }
    }
}

class AuthException(val error: AuthError): Exception()