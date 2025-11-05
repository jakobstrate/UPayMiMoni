package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.model.User

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): AuthResult<User>

    suspend fun registerUser(email: String, password: String): AuthResult<User>

    suspend fun loginUserWithGoogle(idToken: String): AuthResult<User>

    suspend fun sendResetPasswordEmail(email: String): AuthResult<Unit>
}