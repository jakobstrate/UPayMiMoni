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
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.upaymimoni.presentation.ui.ExpenseAddScreen
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen
import com.example.upaymimoni.presentation.ui.auth.screens.ForgotPassScreen
import com.example.upaymimoni.presentation.ui.auth.screens.LoginScreen
import com.example.upaymimoni.presentation.ui.auth.screens.RegisterScreen
import com.example.upaymimoni.presentation.ui.profile.screens.EditProfileScreen
import com.example.upaymimoni.presentation.ui.profile.screens.ProfileScreen
import com.example.upaymimoni.presentation.ui.theme.UPayMiMoniTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

//const val EMULATOR_HOST = "10.0.2.2"

//const val EMULATOR_HOST = "10.126.69.219"
const val EMULATOR_HOST = "192.168.1.129"
const val AUTH_EMULATOR_PORT = 9099
const val FIRESTORE_EMULATOR_PORT = 8080

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
                    val TEST_GROUP_ID = "group1"
                    val TEST_USER_ID = "user1"

                    NavHost(
                        navController = navController,
                        startDestination = NavigationRoutes.authStack
                        //startDestination = "expense_add/$TEST_GROUP_ID/$TEST_USER_ID"
                    ) {
                        // Expense Detail Screen Composable
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
                                onBackClick = { }
                            )
                        }
                        // Expense Add Screen Composable
                        composable(
                            "expense_add/{groupId}/{userId}",
                            arguments = listOf(
                                navArgument("groupId") { type = NavType.StringType },
                                navArgument("userId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            // Retrieve the expenseId from the navigation arguments
                            val groupId =
                                backStackEntry.arguments?.getString("groupId") ?: TEST_GROUP_ID
                            val userId =
                                backStackEntry.arguments?.getString("userId") ?: TEST_USER_ID

                            ExpenseAddScreen(
                                groupId = groupId,
                                userId = userId,
                                onBackClick = { },
                                onExpenseAdded = { }
                            )
                        }

                        navigation(
                            startDestination = NavigationRoutes.loginPage,
                            route = NavigationRoutes.authStack
                        ) {
                            composable(
                                route = NavigationRoutes.loginPage
                            ) {
                                LoginScreen(
                                    onNavigateToRegister = {
                                        navController.navigate(NavigationRoutes.registerPage)
                                    },
                                    onNavigateToHomePage = {
                                        navController.navigate(NavigationRoutes.profileStack) {
                                            popUpTo(NavigationRoutes.authStack) { inclusive = true }
                                        }
                                    },
                                    onNavigateToForgotPass = {
                                        navController.navigate(NavigationRoutes.forgotPassPage)
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
                                        navController.navigate(NavigationRoutes.profileStack) {
                                            popUpTo(NavigationRoutes.authStack) { inclusive = true }
                                        }
                                    }
                                )
                            }

                            composable(
                                route = NavigationRoutes.forgotPassPage
                            ) {
                                ForgotPassScreen(
                                    onBackClick = {
                                        navController.navigate(NavigationRoutes.loginPage)
                                    }
                                )
                            }
                        }

                        navigation(
                            startDestination = NavigationRoutes.profilePage,
                            route = NavigationRoutes.profileStack
                        ) {
                            composable(
                                route = NavigationRoutes.profilePage
                            ) {
                                ProfileScreen(
                                    onNavigateToLogin = {
                                        navController.navigate(NavigationRoutes.authStack) {
                                            popUpTo(0)
                                        }
                                    },
                                    onEditProfileClick = {
                                        navController.navigate(NavigationRoutes.editProfilePage)
                                    },
                                    // TODO: Should navigate to groups page.
                                    onNavigateToGroups = {
                                        navController.navigate(NavigationRoutes.editProfilePage)
                                    }
                                )
                            }

                            composable(
                                route = NavigationRoutes.editProfilePage,
                            ) {
                                EditProfileScreen(
                                    onBackClick = {
                                        navController.navigate(NavigationRoutes.profilePage)
                                    }
                                )
                            }
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
        Firebase.auth.useEmulator(EMULATOR_HOST, AUTH_EMULATOR_PORT)
        Firebase.firestore.useEmulator(EMULATOR_HOST, FIRESTORE_EMULATOR_PORT)
    }
}
