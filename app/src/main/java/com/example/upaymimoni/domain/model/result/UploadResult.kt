package com.example.upaymimoni.domain.model.result

sealed class UploadResult<out T> {
    data class Success<out T>(val data: T) : UploadResult<T>()
    data class Failure(val error: Throwable) : UploadResult<Nothing>()
}