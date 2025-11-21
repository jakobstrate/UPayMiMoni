package com.example.upaymimoni.data.repository

import android.util.Log
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Group
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.example.upaymimoni.domain.repository.GroupRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class FirestoreGroupRepository (
    private val firestore: FirebaseFirestore
) : GroupRepository {
    private val groupCollection = firestore.collection("groups")
    private val EXPENSE_IDS_FIELD = "expenses"

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
        val snapshot = groupCollection
            .document(groupId)
            .get()
            .await()
        val group = snapshot.toObject<Group>()

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

    override suspend fun addExpenseToGroup(
        groupId: String,
        expenseId: String
    ): Result<Unit> = try {
        groupCollection
            .document(groupId)
            .update(EXPENSE_IDS_FIELD, FieldValue.arrayUnion(expenseId))
            .await()
        Log.d("FirestoreGroupRepository", "Successfully linked expense $expenseId to group $groupId")
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("FireStoreError",
            "Failed to add expense $expenseId to group $groupId.", e)
        Result.failure(e)
    }
}