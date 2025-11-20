package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.auth.screens.InitialLoadingScreen

fun NavGraphBuilder.initialLoadingGraph(navController: NavHostController) {
    composable(route = Destination.InitialLoading.route) {
        InitialLoadingScreen(
            onAuthenticated = {
                navController.navigate(Destination.Group.Stack.route) {
                    popUpTo(Destination.InitialLoading.route) { inclusive = true }
                }
            },
            onUnauthenticated = {
                navController.navigate(Destination.Auth.Stack.route) {
                    popUpTo(Destination.InitialLoading.route) { inclusive = true }
                }
            }
        )
    }
}