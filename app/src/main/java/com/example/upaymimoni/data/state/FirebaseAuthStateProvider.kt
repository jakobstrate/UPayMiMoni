package com.example.upaymimoni.data.state

import android.util.Log
import com.example.upaymimoni.domain.state.AuthStateProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthStateProvider(
    private val auth: FirebaseAuth
) : AuthStateProvider {

    override fun getAuthenticatedUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun isSessionValid(): Boolean {
        val user = auth.currentUser ?: return false

        return try {
            user.getIdToken(true).await()
            true
        } catch (e: Exception) {
            Log.w("AuthState", "Token refresh is invalid", e)
            false
        }
    }
}