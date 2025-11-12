package com.example.upaymimoni.domain.model

sealed class UserUpdateResult {
    data class Success(val user: User) : UserUpdateResult()
    data class Failure(val error: UpdateUserError) : UserUpdateResult()
}