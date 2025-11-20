package com.example.upaymimoni.domain.usecase.groups

import com.example.upaymimoni.domain.model.Expense
import com.example.upaymimoni.domain.model.Transaction
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.math.roundToLong

class CalculateSettlementUseCaseTest {
    private val SUT = CalculateSettlementUseCase()

    private val userA = "userA"
    private val userB = "userB"
    private val userC = "userC"
    private val userD = "userD"

    private val testExpenseEven = Expense(
        id = "1",
        name = "Dinner",
        amount = 60.0,
        groupId = "Dinner Group",
        payerUserId = userA,
        splitBetweenUserIds = listOf(userB, userC),
    )

    private val testExpenseDouble = Expense(
        id = "2",
        name = "Date",
        amount = 100.0,
        groupId = "Romantic Dates",
        payerUserId = userA,
        splitBetweenUserIds = listOf(userB)
    )

    private val testExpenseMultiple1 = Expense(
        id = "a1",
        name = "Multiple1",
        amount = 80.0,
        groupId = "Multiple",
        payerUserId = userA,
        splitBetweenUserIds = listOf(userB, userC, userD)
    )

    private val testExpenseMultiple2 = Expense(
        id = "a2",
        name = "Multiple2",
        amount = 40.0,
        groupId = "Multiple",
        payerUserId = userA,
        splitBetweenUserIds = listOf(userB, userC, userD)
    )

    private val testExpenseMultipleDifferent = Expense(
        id = "b1",
        name = "Multiple3",
        amount = 120.0,
        groupId = "Multiple",
        payerUserId = userB,
        splitBetweenUserIds = listOf(userA, userC, userD)
    )

    private val testExpenseWithDecimals = Expense(
        id = "3",
        name = "Decimal1",
        amount = 50.5,
        groupId = "Decimal",
        payerUserId = userC,
        splitBetweenUserIds = listOf(userA, userD)
    )

    private val testExpenseForInvoke1 = Expense(
        id = "1",
        name = "Invoke1",
        amount = 60.0,
        payerUserId = userA,
        groupId = "Invoke1",
        splitBetweenUserIds = listOf(userB, userC)
    )

    private val testExpenseForInvoke2 = Expense(
        id = "2",
        name = "Invoke2",
        amount = 30.0,
        payerUserId = userB,
        groupId = "Invoke1",
        splitBetweenUserIds = listOf(userA, userC)
    )

    private val testExpenseForInvoke3 = Expense(
        id = "3",
        name = "Invoke3",
        amount = 40.0,
        payerUserId = userC,
        groupId = "Invoke1",
        splitBetweenUserIds = listOf(userA, userB)
    )


    @Test
    fun calculateBalancesSingleExpenseSplitEvenly() {
        val expenses = listOf(testExpenseEven)
        val result = SUT.calculateBalances(expenses)

        assertEquals(40.0, result[userA])
        assertEquals(-20.0, result[userB])
        assertEquals(-20.0, result[userC])
    }

    @Test
    fun calculateBalanceShouldIncludePayerAutomatically() {
        val expenses = listOf(testExpenseDouble)
        val result = SUT.calculateBalances(expenses)

        assertEquals(50.0, result[userA])
        assertEquals(-50.0, result[userB])
    }

    @Test
    fun calculateBalanceShouldHandleMultipleExpensesWithSamePayer() {
        val expenses = listOf(testExpenseMultiple1, testExpenseMultiple2)
        val result = SUT.calculateBalances(expenses)

        assertEquals(90.0, result[userA])
        assertEquals(-30.0, result[userB])
        assertEquals(-30.0, result[userC])
        assertEquals(-30.0, result[userD])
    }

    @Test
    fun calculateBalanceShouldHandleMultipleExpensesWithDifferentPayers() {
        val expenses =
            listOf(testExpenseMultiple1, testExpenseMultiple2, testExpenseMultipleDifferent)
        val result = SUT.calculateBalances(expenses)

        assertEquals(60.0, result[userA])
        assertEquals(60.0, result[userB])
        assertEquals(-60.0, result[userC])
        assertEquals(-60.0, result[userD])
    }

    @Test
    fun calculateBalanceShouldHandleSingleExpenseWithDecimals() {
        val expenses = listOf(testExpenseWithDecimals)
        val result = SUT.calculateBalances(expenses)

        val share =
            testExpenseWithDecimals.amount / (testExpenseWithDecimals.splitBetweenUserIds.size + 1)
        val total = testExpenseWithDecimals.amount

        assertEquals((total - share), result[userC])
        assertEquals((-share), result[userA])
        assertEquals((-share), result[userD])
    }

    @Test
    fun minimizeTransactionsShouldReturn2TransactionsWithEvenExpense() {
        val balance = mapOf(
            userA to 40.0,
            userB to -20.0,
            userC to -20.0
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransaction1 = Transaction(userB, userA, 20.0)
        val expectedTransaction2 = Transaction(userC, userA, 20.0)

        assert(result.contains(expectedTransaction1))
        assert(result.contains(expectedTransaction2))

        assertEquals(2, result.size)
    }

    @Test
    fun minimizeTransactionShouldReturn1TransactionsWhenMatched() {
        val balance = mapOf(
            userA to 50.0,
            userB to -50.0
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransaction = Transaction(userB, userA, 50.0)

        assert(result.contains(expectedTransaction))

        assertEquals(1, result.size)
    }

    @Test
    fun minimizeTransactionShouldReturnCorrectTransactionsWithMultipleCreditorsSingleDebitor() {
        val balance = mapOf(
            userA to 30.0,
            userB to -50.0,
            userC to 10.0,
            userD to 10.0,
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransaction1 = Transaction(userB, userA, 30.0)
        val expectedTransaction2 = Transaction(userB, userC, 10.0)
        val expectedTransaction3 = Transaction(userB, userD, 10.0)

        val expectedTransactions = setOf(
            expectedTransaction1, expectedTransaction2, expectedTransaction3
        )

        assert(result.containsAll(expectedTransactions))
        assertEquals(3, result.size)
    }

    @Test
    fun minimizeTransactionShouldReturnCorrectTransactionsWithSingleCreditorsMultipleDebitor() {
        val balance = mapOf(
            userA to 60.0,
            userB to -30.0,
            userC to -10.0,
            userD to -20.0,
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransactions = setOf(
            Transaction(userB, userA, 30.0),
            Transaction(userC, userA, 10.0),
            Transaction(userD, userA, 20.0)
        )

        assert(result.containsAll(expectedTransactions))
        assertEquals(3, result.size)
    }

    @Test
    fun minimizeTransactionShouldReturnCorrectTransactionsWithMultipleCreditorsMultipleDebtors() {
        val balance = mapOf(
            userA to 60.0,
            userB to -30.0,
            userC to -40.0,
            userD to 10.0,
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransactions = setOf(
            Transaction(userC, userA, 40.0),
            Transaction(userB, userA, 20.0),
            Transaction(userB, userD, 10.0)
        )

        assert(result.containsAll(expectedTransactions))
        assertEquals(3, result.size)
    }

    @Test
    fun minimizeTransactionsShouldReturnEmptyListWhenAllBalancesZero() {
        val balance = mapOf(
            userA to 0.0,
            userB to 0.0,
            userC to 0.0,
            userD to 0.0
        )

        val result = SUT.minimizeTransactions(balance)

        assertEquals(0, result.size)
        assert(result.isEmpty())
    }

    @Test
    fun minimizeTransactionsShouldHandleFloatingPointBalancesCorrectly() {
        val balance = mapOf(
            userA to 33.33,
            userB to -11.11,
            userC to -22.22
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransactions = setOf(
            Transaction(userB, userA, 11.11),
            Transaction(userC, userA, 22.22)
        )

        assert(result.containsAll(expectedTransactions))
        assertEquals(2, result.size)
    }

    @Test
    fun minimizeTransactionsShouldReturnCorrectTransactionsWithPartialSettlements() {
        val balance = mapOf(
            userA to 50.0,
            userB to 20.0,
            userC to -30.0,
            userD to -40.0
        )

        val result = SUT.minimizeTransactions(balance)

        val expectedTransactions = setOf(
            Transaction(userD, userA, 40.0),
            Transaction(userC, userB, 20.0),
            Transaction(userC, userA, 10.0)
        )

        assert(result.containsAll(expectedTransactions))
        assertEquals(3, result.size)
    }

    @Test
    fun invokeShouldReturnCorrectTransactionsForSingularExpense() = runBlocking {
        val expenses = listOf(testExpenseForInvoke1)

        val result = SUT(expenses)

        val expectedTransactions = setOf(
            Transaction(userB, userA, 20.0),
            Transaction(userC, userA, 20.0)
        )

        assert(result.containsAll(expectedTransactions))
    }

    @Test
    fun invokeShouldReturnCorrectTransactionsForMultipleExpenses() = runBlocking {
        val expenses = listOf(testExpenseForInvoke1, testExpenseForInvoke2, testExpenseForInvoke3)

        val result = SUT(expenses)

        /*
        Manually calculated expected transactions:
        Expense 1: 60 / 3 = 20.0, userA paying
            userA 40.0
            userB -20.0
            userC -20.0
        Expense 2: 30 / 3 = 10.0, userB paying
            userA 30.0
            userB 0.0
            userC -30.0
        Expense 3: 40 / 3 = 13.333, userC paying
            userA 16.667
            userB -13.333
            userC -3.333
         */
        val expectedTransactions = setOf(
            Transaction(userB, userA, 13.333),
            Transaction(userC, userA, 3.333)
        )

        val roundedResult = result.map { t ->
            t.copy(amount = ((t.amount * 1000.0).roundToLong() / 1000.0))
        }.toSet()

        println(roundedResult)
        assertEquals(expectedTransactions, roundedResult)
    }
}