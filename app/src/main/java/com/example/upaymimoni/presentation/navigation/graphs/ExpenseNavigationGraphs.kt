package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.ExpenseAddScreen
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen

fun NavGraphBuilder.expenseDetailGraph(navController: NavHostController) {
    composable(
        route = Destination.Expense.Detail.route,
        arguments = listOf(
            navArgument(Destination.Expense.Detail.ARG_EXPENSE_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val expenseId = backStackEntry.arguments?.getString(
            Destination.Expense.Detail.ARG_EXPENSE_ID
        ) ?: error("expenseId is required")

        ExpenseDetailScreen(
            expenseId = expenseId,
            onBackClick = { navController.navigateUp() }
        )
    }
}

fun NavGraphBuilder.expenseAddGraph(navController: NavHostController) {
    composable(
        route = Destination.Expense.Add.route,
        arguments = listOf(
            navArgument(Destination.Expense.Add.ARG_GROUP_ID) {
                type = NavType.StringType
                                                              },
            navArgument(Destination.Expense.Add.ARG_USER_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val groupId = backStackEntry.arguments?.getString(
            Destination.Expense.Add.ARG_GROUP_ID
        ) ?: error("groupId is required")
        val userId = backStackEntry.arguments?.getString(
            Destination.Expense.Add.ARG_USER_ID
        ) ?: error("userId is required")

        ExpenseAddScreen(
            groupId = groupId,
            userId = userId,
            onBackClick = { navController.navigateUp() },
            onExpenseAdded = {}
        )
    }
}
