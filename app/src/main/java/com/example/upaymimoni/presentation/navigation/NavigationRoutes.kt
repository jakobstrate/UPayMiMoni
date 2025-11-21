package com.example.upaymimoni.presentation.navigation
// AI SUMMARY
/**
 * Type-safe navigation destinations.
 *
 * ## Usage Examples:
 *
 * ### Simple Navigation:
 * ```kotlin
 * navController.navigate(Destination.Auth.Login.route)
 * navController.navigate(Destination.Profile.Home.route)
 * ```
 *
 * ### Navigation with Parameters:
 * ```kotlin
 * navController.navigate(Destination.Expense.Detail.createRoute(expenseId = "123"))
 * navController.navigate(Destination.Expense.Add.createRoute(groupId = "group1", userId = "user1"))
 * ```
 *
 * ### In NavGraphBuilder:
 * ```kotlin
 * composable(
 *     route = Destination.Expense.Detail.route,
 *     arguments = listOf(
 *         navArgument(Destination.Expense.Detail.ARG_EXPENSE_ID) {
 *             type = NavType.StringType
 *         }
 *     )
 * ) { backStackEntry ->
 *     val expenseId = backStackEntry.arguments?.getString(
 *         Destination.Expense.Detail.ARG_EXPENSE_ID
 *     ) ?: error("expenseId is required")
 *     // Use expenseId...
 * }
 * ```
 */
sealed interface Destination {
    val route: String

    data object InitialLoading : Destination {
        override val route = "010010010101010101010011010001010100010001000001010010010101010001001111010101110101001001001001010101000100010101010100010010000100100101010011"
    }

    sealed class Auth(override val route: String) : Destination {
        data object Stack : Auth("AuthStack")
        data object Login : Auth("LoginPage")
        data object Register : Auth("RegisterPage")
        data object ForgotPassword : Auth("ForgotPasswordPage")
    }

    sealed class Profile(override val route: String) : Destination {
        data object Stack : Profile("ProfileStack")
        data object Home : Profile("ProfileHome")
        data object Edit : Profile("ProfileEdit")
    }

    sealed class Group(override val route: String) : Destination {
        data object Stack : Group("GroupStack")
        data object Overview : Group("GroupsOverview")

        data object Instance : Group("GroupInstance/{groupId}") {
            fun createRoute(groupId: String) = "GroupInstance/$groupId"

            const val ARG_GROUP_ID = "groupId"
        }

        data object Edit : Group("GroupEdit/{groupId}") {
            fun createRoute(groupId: String) = "GroupEdit/$groupId"

            const val ARG_GROUP_ID = "groupId"
        }
    }

    sealed class Expense(override val route: String) : Destination {
        data object Detail : Expense("ExpenseDetail/{expenseId}") {
            fun createRoute(expenseId: String) = "ExpenseDetail/$expenseId"

            const val ARG_EXPENSE_ID = "expenseId"
        }

        data object Add : Expense("ExpenseAdd/{groupId}/{userId}") {
            fun createRoute(groupId: String, userId: String) = "ExpenseAdd/$groupId/$userId"

            const val ARG_GROUP_ID = "groupId"
            const val ARG_USER_ID = "userId"
        }
    }
}