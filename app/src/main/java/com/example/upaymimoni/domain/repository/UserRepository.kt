package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.User

interface UserRepository {
    fun saveUser(user: User)
    fun getUser(userId: String): Result<User>
}