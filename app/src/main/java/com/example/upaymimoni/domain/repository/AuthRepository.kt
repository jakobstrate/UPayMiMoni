package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.User

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun registerUser(email: String, password: String)
}