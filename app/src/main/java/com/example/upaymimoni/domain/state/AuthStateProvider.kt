package com.example.upaymimoni.domain.state

interface AuthStateProvider {
    fun isAuthenticated(): Boolean
    fun getAuthenticatedUserId(): String?
}