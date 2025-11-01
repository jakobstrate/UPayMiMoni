package com.example.upaymimoni

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen
import com.example.upaymimoni.presentation.ui.auth.LoginScreen
import com.example.upaymimoni.presentation.ui.auth.RegisterScreen
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

const val EMULATOR_HOST = "10.0.0.2"

//    val EMULATOR_HOST = "192.168.1.129"
const val EMULATOR_PORT = 9099

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureFirebaseEmulator()

        enableEdgeToEdge()
        setContent {
            UPayMiMoniTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val TEST_EXPENSE_ID = "mock1"

                    NavHost(
                        navController = navController,
//                        startDestination = "expense_detail/$TEST_EXPENSE_ID") {
                        startDestination = "Login"
                    ) {
// 1. Expense Detail Screen Composable
                        composable(
                            "expense_detail/{expenseId}",
                            arguments = listOf(navArgument("expenseId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            // Retrieve the expenseId from the navigation arguments
                            val expenseId =
                                backStackEntry.arguments?.getString("expenseId") ?: TEST_EXPENSE_ID

                            ExpenseDetailScreen(
                                expenseId = expenseId,
                                // Since this is the only screen, onBackClick will just close the app (or navigate nowhere)
                                onBackClick = { /* No-op or finish Activity */ }
                            )
                        }
                        composable(
                            route = NavigationRoutes.loginPage
                        ) {
                            LoginScreen(
                                onNavigateToRegister = {
                                    navController.navigate(NavigationRoutes.registerPage)
                                },
                                onNavigateToHomePage = {
                                    navController.navigate("expense_detail/$TEST_EXPENSE_ID")
                                }
                            )
                        }

                        composable(
                            route = NavigationRoutes.registerPage
                        ) {
                            RegisterScreen(
                                onNavigateToLogin = {
                                    navController.navigate(NavigationRoutes.loginPage)
                                },
                                onNavigateToHomePage = {
                                    navController.navigate("expense_detail/$TEST_EXPENSE_ID")
                                }
                            )
                        }

                    }
                }
            }
        }
    }

    /**
     * This configures firebase auth to run on a emulator.
     *
     * An emulator has to be setup and running on <EMULATOR_HOST>:<EMULATOR_PORT> for this to work.
     */
    private fun configureFirebaseEmulator() {
        FirebaseApp.initializeApp(this)
        Firebase.auth.useEmulator(EMULATOR_HOST, EMULATOR_PORT)
    }
}
