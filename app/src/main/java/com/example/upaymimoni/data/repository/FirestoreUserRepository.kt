package com.example.upaymimoni.data.repository

import android.util.Log
import com.example.upaymimoni.domain.model.User
import com.example.upaymimoni.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun saveUser(user: User): Result<Unit> = try {
        usersCollection
            .document(user.id)
            .set(user)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUser(userId: String): Result<User> = try {
        val snapshot = usersCollection
            .document(userId)
            .get()
            .await()
        val user = snapshot.toObject<User>()

        if (user != null) {
            Result.success(user)
        } else {
            Log.e(
                "DatabaseImpl",
                "Did not find user: $userId in the database."
            )
            Result.failure(NoSuchElementException("User $userId not found"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}