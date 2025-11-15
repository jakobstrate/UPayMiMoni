package com.example.upaymimoni.data.repository

import com.example.upaymimoni.data.dto.BaseNotificationDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PushNotificationApi {
    @POST("notifications/send")
    suspend fun sendNotification(
        @Body body: BaseNotificationDTO
    ): Response<Unit>
}