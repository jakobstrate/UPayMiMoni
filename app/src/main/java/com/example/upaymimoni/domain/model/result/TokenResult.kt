package com.example.upaymimoni.domain.model.result

sealed class TokenResult<out T> {
    data class Success<out T>(val data: T) : TokenResult<T>()
    data class Failure(val error: Throwable) : TokenResult<Nothing>()
}