package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.model.result.UserUpdateResult

interface UserRepository {
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun getUser(userId: String): Result<User>
    suspend fun updateUser(
        userId: String,
        newDisplayName: String,
        newEmail: String,
        newPhone: String
    ): UserUpdateResult

    suspend fun updateUserProfilePicture(
        userId: String,
        newProfilePictureUrl: String
    ): UserUpdateResult
}