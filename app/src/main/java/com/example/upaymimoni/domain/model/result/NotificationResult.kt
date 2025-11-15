package com.example.upaymimoni.domain.model.result

sealed class NotificationResult {
    object Success : NotificationResult()
    object NoTokens : NotificationResult()
    data class HttpError(val code: Int, val message: String?) : NotificationResult()
    data class NetworkError(val exception: Exception) : NotificationResult()
}