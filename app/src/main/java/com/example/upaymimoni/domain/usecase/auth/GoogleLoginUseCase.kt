package com.example.upaymimoni.domain.usecase.auth

import com.example.upaymimoni.domain.model.AuthResult
import com.example.upaymimoni.domain.repository.AuthRepository

class GoogleLoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(idToken: String): AuthResult {
        return repo.loginUserWithGoogle(idToken)
    }
}