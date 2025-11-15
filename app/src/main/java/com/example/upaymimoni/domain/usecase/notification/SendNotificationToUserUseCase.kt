package com.example.upaymimoni.domain.usecase.notification

import com.example.upaymimoni.domain.model.BaseNotificationBody
import com.example.upaymimoni.domain.model.result.NotificationResult
import com.example.upaymimoni.domain.model.result.TokenResult
import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.example.upaymimoni.domain.repository.PushNotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okio.IOException
import kotlin.math.pow

class SendNotificationToUserUseCase(
    private val sendRepository: PushNotificationRepository,
    private val fcmTokenRepository: FcmTokenRepository
) {
    suspend operator fun invoke(
        userId: String,
        notificationBody: BaseNotificationBody
    ): NotificationResult = withContext(Dispatchers.IO) {
        val tokensResult = fcmTokenRepository.getTokensForUser(userId)
        if (tokensResult is TokenResult.Failure) return@withContext NotificationResult.NoTokens

        val tokens = (tokensResult as TokenResult.Success).data

        if (tokens.isEmpty()) return@withContext NotificationResult.NoTokens

        var sendSuccess = false

        tokens.forEach { token ->
            val success = retrySend(token, notificationBody)
            if (success) sendSuccess = true
        }

        if (sendSuccess) return@withContext NotificationResult.Success
        return@withContext NotificationResult.NetworkError(
            IOException("Failed to send to all devices")
        )
    }

    private suspend fun retrySend(
        token: String,
        content: BaseNotificationBody,
        maxRetries: Int = 3,
        baseDelayMilli: Long = 5000L
    ): Boolean {
        repeat(maxRetries) { attempt ->
            val result = sendRepository.sendNotification(token, content)

            when (result) {
                is NotificationResult.Success -> return true
                is NotificationResult.NetworkError -> {
                    if (attempt >= maxRetries - 1) return false
                    val delayTime = baseDelayMilli * 2.0.pow(attempt.toDouble()).toLong()
                    delay(delayTime)
                }

                else -> return false
            }
        }
        return false
    }
}