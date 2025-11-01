package com.example.upaymimoni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.upaymimoni.presentation.ui.ExpenseAddScreen
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen
import com.example.upaymimoni.presentation.ui.auth.LoginScreen
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            UPayMiMoniTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val TEST_EXPENSE_ID = "mock1"
                    val TEST_GROUP_ID = "group1"
                    val TEST_USER_ID = "user1"

                    NavHost(
                        navController = navController,
                        startDestination = "expense_add/$TEST_GROUP_ID/$TEST_USER_ID"
//                        startDestination = "Login"
                    ) {
                        // Expense Detail Screen Composable
                        composable(
                            "expense_detail/{expenseId}",
                            arguments = listOf(navArgument("expenseId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the expenseId from the navigation arguments
                            val expenseId = backStackEntry.arguments?.getString("expenseId") ?: TEST_EXPENSE_ID

                            ExpenseDetailScreen(
                                expenseId = expenseId,
                                onBackClick = { }
                            )
                        }
                        // Expense Add Screen Composable
                        composable(
                            "expense_add/{groupId}/{userId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.StringType },
                                navArgument("userId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the expenseId from the navigation arguments
                            val groupId = backStackEntry.arguments?.getString("groupId") ?: TEST_GROUP_ID
                            val userId = backStackEntry.arguments?.getString("userId") ?: TEST_USER_ID

                            ExpenseAddScreen(
                                groupId = groupId,
                                userId = userId,
                                onBackClick = { },
                                onExpenseAdded = { }
                            )
                        }
                        composable(
                            "Login"
                        ) {
                            LoginScreen()
                        }

                    }
                }
            }
        }
    }
}
