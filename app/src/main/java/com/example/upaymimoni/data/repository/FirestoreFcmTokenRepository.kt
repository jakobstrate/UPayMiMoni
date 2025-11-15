package com.example.upaymimoni.data.repository

import com.example.upaymimoni.domain.model.result.TokenResult
import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreFcmTokenRepository(
    private val firestore: FirebaseFirestore
) : FcmTokenRepository {
    override suspend fun saveToken(
        userId: String,
        deviceId: String,
        token: String
    ): TokenResult<Unit> {
        return try {
            val deviceRef = firestore
                .collection("userDevices")
                .document(userId)
                .collection("devices")
                .document(deviceId)

            val data = mapOf(
                "token" to token,
                "lastUpdated" to System.currentTimeMillis()
            )

            deviceRef.set(data).await()
            TokenResult.Success(Unit)
        } catch (e: Exception) {
            TokenResult.Failure(e)
        }
    }

    override suspend fun getTokensForUser(userId: String): TokenResult<List<String>> {
        return try {
            val snapshot = firestore
                .collection("userDevices")
                .document(userId)
                .collection("devices")
                .get()
                .await()

            val tokens = snapshot.documents.mapNotNull { it.getString("token") }

            TokenResult.Success(tokens)
        } catch (e: Exception) {
            TokenResult.Failure(e)
        }
    }
}