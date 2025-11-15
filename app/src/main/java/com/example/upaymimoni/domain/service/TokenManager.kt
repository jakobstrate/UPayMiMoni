package com.example.upaymimoni.domain.service

interface TokenManager {
    suspend fun fetchAndSaveToken(userId: String)
}