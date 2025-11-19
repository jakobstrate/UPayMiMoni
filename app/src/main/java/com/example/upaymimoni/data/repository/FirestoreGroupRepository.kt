package com.example.upaymimoni.data.repository

import android.util.Log
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirestoreGroupRepository (
    private val firestore: FirebaseFirestore
) : GroupRepository {
    private val groupCollection = firestore.collection("groups")

    override suspend fun saveGroup(group: Group): Result<Unit> = try {
        //try to add expense to firestore collection
        groupCollection
            .document(group.id)
            .set(group)
            .await()
        Result.success(Unit)
    } catch (e: Exception){
        Log.e("FireStoreError",
            "Could not add group:${group.id} to firestore",e)
        Result.failure(e)
    }

    override suspend fun getGroup(groupId: String): Result<Group> = try {
        println("Before Access")
        val snapshot = groupCollection
            .document(groupId)
            .get()
            .await()
        val group = snapshot.toObject<Group>()

        println("After Access")

        if (group != null) {
            Result.success(group)
        } else {
            Log.e(
                "DatabaseImpl",
                "Did not find group: $groupId in the database."
            )
            Result.failure(NoSuchElementException("Group $groupId not found"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}