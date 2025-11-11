package com.example.upaymimoni.data.repository

import android.util.Log
import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.repository.ExpenseRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class FirestoreExpenseRepository (
    private val firestore: FirebaseFirestore
) : ExpenseRepository {

    //collection to use inside firestore
    private val expensesCollection = firestore.collection("expenses")


    override suspend fun addExpense(expense: Expense): Result<Unit> = try {
        //try to add expense to firestore collection
        expensesCollection
            .document(expense.id)
            .set(expense)
            .await()
        Result.success(Unit)
    } catch (e: Exception){
        Log.e("FireStoreError",
            "Could not add expense:${expense.id} to firestore",e)
        Result.failure(e)
    }


    override fun getExpensesForGroup(groupId: String): Flow<List<Expense>> {
        TODO("Not yet implemented")
    }

    override fun getExpensesOfUserForGroup(
        userId: String,
        groupId: String
    ): Flow<List<Expense>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExpenseById(id: String): Result<Expense> = try {
        val documentSnapshot = expensesCollection
            .document(id)
            .get()
            .await()
        // map the snapshot data to Expense data class
        val expense = documentSnapshot.toObject<Expense>()

        // expense found, and could be mapped to object
        if (expense != null) {
            Result.success(expense)
        } else {
            // expense not found
            Log.e(
                "ExpenseNotFound",
                "Did not find expense: $id in the database."
            )
            Result.failure(NoSuchElementException("Expense $id not found"))
        }
    } catch (e: Exception) {
        Log.e("FirestoreError", "Error fetching expense $id", e)
        Result.failure(e)
    }
}