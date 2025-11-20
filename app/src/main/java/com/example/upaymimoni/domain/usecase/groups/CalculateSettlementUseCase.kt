package com.example.upaymimoni.domain.usecase.groups

import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Transaction
import java.util.PriorityQueue

class CalculateSettlementUseCase(

) {
    suspend operator fun invoke(expenses: List<Expense>): List<Transaction> {
        val balance = calculateBalances(expenses)

        val transactions = minimizeTransactions(balance)

        return transactions
    }

    /**
     * Calculates the balances between members by iterating over all expenses
     * and keeping track of each users contributions relative to a common pool.
     *
     * @return A map that correlates UserId's to what they either are owed or owe.
     * If the number is positive it means they are owed, negative means they owe.
     */
    fun calculateBalances(expenses: List<Expense>): Map<String, Double> {
        val balance = mutableMapOf<String, Double>()

        for (e in expenses) {
            val splitCount = e.splitBetweenUserIds.size + 1
            val totalCost = e.amount
            val shareCost = totalCost / splitCount

            val creditorBalance = balance.getOrDefault(e.payerUserId, 0.0)
            balance[e.payerUserId] = creditorBalance + (totalCost - shareCost)

            for (u in e.splitBetweenUserIds) {
                val debtorBalance = balance.getOrDefault(u, 0.0)
                balance[u] = debtorBalance - shareCost
            }
        }

        return balance
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun minimizeTransactions(balance: Map<String, Double>): List<Transaction> {
        val creditors = PriorityQueue<Pair<String, Double>> { a, b ->
            b.second.compareTo(a.second)
        }

        val debtors = PriorityQueue<Pair<String, Double>> { a, b ->
            b.second.compareTo(a.second)
        }

        balance.forEach { (userId, amount) ->
            when {
                amount > 0 -> creditors.add(userId to amount)
                amount < 0 -> debtors.add(userId to -amount)
            }
        }

        val transactions = mutableListOf<Transaction>()

        while (creditors.isNotEmpty() && debtors.isNotEmpty()) {
            val (creditorId, creditorAmount) = creditors.poll()
            val (debtorId, debtorAmount) = debtors.poll()

            val settleAmount = minOf(creditorAmount, debtorAmount)

            val transaction = Transaction(
                fromUserId = debtorId,
                toUserId = creditorId,
                amount = settleAmount
            )

            transactions.add(transaction)

            val remainingCreditor = creditorAmount - settleAmount
            val remainingDebtor = debtorAmount - settleAmount

            // Check for floating point errors.

            if (remainingCreditor > 1e-9) {
                creditors.add(creditorId to remainingCreditor)
            }

            if (remainingDebtor > 1e-9) {
                debtors.add(debtorId to remainingDebtor)
            }
        }

        return transactions
    }
}