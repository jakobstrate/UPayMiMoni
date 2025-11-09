package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepo: AuthRepository
) {
    suspend operator fun invoke() {
        authRepo.logoutUser()
    }
}