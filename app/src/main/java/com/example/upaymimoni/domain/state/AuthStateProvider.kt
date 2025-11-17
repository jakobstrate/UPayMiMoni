package com.example.upaymimoni.domain.state

interface AuthStateProvider {

    fun getAuthenticatedUserId(): String?

    suspend fun isSessionValid(): Boolean
}