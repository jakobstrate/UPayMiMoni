package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.repository.AuthRepository
import com.example.upaymimoni.domain.session.UserSession

class LogoutUseCase(
    private val authRepo: AuthRepository,
    private val userSession: UserSession,
) {
    suspend operator fun invoke() {
        userSession.clearUser()
        authRepo.logoutUser()
    }
}