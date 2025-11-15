package com.example.upaymimoni.domain.model.result

import com.example.upaymimoni.domain.model.UpdateUserError
import com.example.upaymimoni.domain.model.User

sealed class UserUpdateResult {
    data class Success(val user: User) : UserUpdateResult()
    data class Failure(val error: UpdateUserError) : UserUpdateResult()
}