package com.example.upaymimoni.data.state

import com.example.upaymimoni.domain.state.AuthStateProvider
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthStateProvider(
    private val auth: FirebaseAuth
) : AuthStateProvider {
    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override fun getAuthenticatedUserId(): String? {
        return auth.currentUser?.uid
    }
}