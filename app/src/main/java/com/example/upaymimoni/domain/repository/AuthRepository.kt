package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.AuthResult

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): AuthResult

    suspend fun registerUser(email: String, password: String): AuthResult

    suspend fun loginUserWithGoogle(idToken: String): AuthResult
}