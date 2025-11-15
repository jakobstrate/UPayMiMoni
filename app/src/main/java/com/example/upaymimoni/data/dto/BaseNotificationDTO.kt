package com.example.upaymimoni.data.dto

import com.example.upaymimoni.domain.model.BaseNotificationBody

data class BaseNotificationDTO(
    val recipientToken: String,
    val notificationBody: BaseNotificationBody
)