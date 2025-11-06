package com.example.upaymimoni.domain.model

/**
 * Generic Result wrapper so that we can return anything + an AuthException
 */
sealed class AuthResult<out T> {
    data class Success<out T>(val data: T): AuthResult<T>()
    data class Failure(val error: AuthException): AuthResult<Nothing>()
}