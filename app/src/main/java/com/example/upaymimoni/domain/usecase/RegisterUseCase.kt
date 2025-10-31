package com.example.upaymimoni.domain.usecase

import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.AuthRepository

class RegisterUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke() {}
}
