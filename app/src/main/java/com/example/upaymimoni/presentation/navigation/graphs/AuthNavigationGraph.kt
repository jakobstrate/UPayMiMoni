package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.auth.screens.ForgotPassScreen
import com.example.upaymimoni.presentation.ui.auth.screens.LoginScreen
import com.example.upaymimoni.presentation.ui.auth.screens.RegisterScreen

fun NavGraphBuilder.authNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = Destination.Auth.Login.route,
        route = Destination.Auth.Stack.route
    ) {
        composable(route = Destination.Auth.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Destination.Auth.Register.route)
                },
                onNavigateToHomePage = {
                    navController.navigate(Destination.Group.Stack.route) {
                        popUpTo(Destination.Auth.Stack.route) { inclusive = true }
                    }
                },
                onNavigateToForgotPass = {
                    navController.navigate(Destination.Auth.ForgotPassword.route)
                }
            )
        }

        composable(route = Destination.Auth.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Destination.Auth.Login.route)
                },
                onNavigateToHomePage = {
                    navController.navigate(Destination.Group.Stack.route) {
                        popUpTo(Destination.Auth.Stack.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Destination.Auth.ForgotPassword.route) {
            ForgotPassScreen(
                onBackClick = {
                    navController.navigate(Destination.Auth.Login.route)
                }
            )
        }
    }

}