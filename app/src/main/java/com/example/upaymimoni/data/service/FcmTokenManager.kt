package com.example.upaymimoni.data.service

import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.example.upaymimoni.domain.service.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FcmTokenManager(
    private val tokenRepository: FcmTokenRepository,
    private val deviceIdService: DeviceIdService,
    private val messaging: FirebaseMessaging
) : TokenManager {
    override suspend fun fetchAndSaveToken(userId: String) {
        val deviceId = deviceIdService.deviceId

        val tokenResult = messaging.token.await()

        tokenRepository.saveToken(userId, deviceId, tokenResult)
    }
}