package com.example.upaymimoni.domain.model

sealed class AuthResult {
    data class Success(val user: User): AuthResult()
    data class Failure(val exception: AuthException): AuthResult()
}