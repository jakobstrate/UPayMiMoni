package com.example.upaymimoni.data.repository

import com.example.upaymimoni.data.dto.BaseNotificationDTO
import com.example.upaymimoni.domain.model.BaseNotificationBody
import com.example.upaymimoni.domain.model.result.NotificationResult
import com.example.upaymimoni.domain.repository.PushNotificationRepository
import okio.IOException

class PushNotificationRepositoryImpl(
    val api: PushNotificationApi
) : PushNotificationRepository {
    override suspend fun sendNotification(
        recipientToken: String,
        content: BaseNotificationBody
    ) : NotificationResult {
        val dto = BaseNotificationDTO(
            recipientToken = recipientToken,
            notificationBody = content
        )

        return try {
            val response = api.sendNotification(dto)

            if (response.isSuccessful) {
                NotificationResult.Success
            } else {
                NotificationResult.HttpError(
                    code = response.code(),
                    message = response.errorBody()?.string()
                )
            }
        } catch (e: IOException) {
            NotificationResult.NetworkError(e)
        } catch (e: Exception) {
            NotificationResult.HttpError(-1, e.message)
        }
    }
}