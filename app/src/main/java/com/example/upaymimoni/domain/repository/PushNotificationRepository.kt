package com.example.upaymimoni.domain.repository

import com.example.upaymimoni.domain.model.BaseNotificationBody
import com.example.upaymimoni.domain.model.result.NotificationResult

interface PushNotificationRepository {
    suspend fun sendNotification(recipientToken: String, content: BaseNotificationBody): NotificationResult
}