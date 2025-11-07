package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getUser(userId: String): Result<User>
}