package com.example.upaymimoni.data.repository

import com.example.upaymimoni.domain.model.AuthErrorType
import com.example.upaymimoni.domain.model.AuthException
import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun loginUser(email: String, password: String): AuthResult = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Login Auth returned null user")

        AuthResult.Success(
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email
            )
        )

    } catch (t: Throwable) {
        AuthResult.Failure(mapExceptionToDomain(t))
    }

    override suspend fun registerUser(email: String, password: String): AuthResult = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw Exception("Register Auth returned null user")

        AuthResult.Success(
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email
            )
        )

    }
    catch (t: Throwable) {
        AuthResult.Failure(mapExceptionToDomain(t))
    }

    /**
     * Method that maps Firebase Exceptions to our domain @see AuthException.
     */
    private fun mapExceptionToDomain(t: Throwable): AuthException = when (t) {
        is FirebaseAuthInvalidUserException -> AuthException(AuthErrorType.InvalidUser, t.message)
        is FirebaseNetworkException -> AuthException(AuthErrorType.NetworkFailure, t.message)
        is FirebaseTooManyRequestsException -> AuthException(AuthErrorType.TooManyLogins, t.message)
        is FirebaseAuthUserCollisionException -> AuthException(AuthErrorType.EmailInUse, t.message)
        is IllegalArgumentException -> AuthException(AuthErrorType.EmptyOrNull, t.message)
        is FirebaseAuthWeakPasswordException -> AuthException(AuthErrorType.WeakPassword, t.reason)
        is FirebaseAuthInvalidCredentialsException -> when (t.errorCode) {
            "ERROR_WRONG_PASSWORD" -> AuthException(AuthErrorType.InvalidCredentials, t.message)
            "ERROR_INVALID_EMAIL" -> AuthException(AuthErrorType.InvalidEmailFormat, t.message)
            else -> AuthException(
                AuthErrorType.Unknown,
                t.message ?: "An Unknown or Unmapped Firebase Error Occurred"
            )
        }

        else -> AuthException(
            AuthErrorType.Unknown,
            t.message ?: "An Unknown or Unmapped Firebase Error Occurred"
        )
    }
}