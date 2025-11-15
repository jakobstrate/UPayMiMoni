package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.result.TokenResult

interface FcmTokenRepository {
    suspend fun saveToken(userId: String, deviceId: String, token: String): TokenResult<Unit>
    suspend fun getTokensForUser(userId: String): TokenResult<List<String>>
}