package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.profile.screens.EditProfileScreen
import com.example.upaymimoni.presentation.ui.profile.screens.ProfileScreen

fun NavGraphBuilder.profileNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = Destination.Profile.Home.route,
        route = Destination.Profile.Stack.route
    ) {
        composable(route = Destination.Profile.Home.route) {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Destination.Auth.Stack.route) { popUpTo(0) }
                },
                onEditProfileClick = {
                    navController.navigate(Destination.Profile.Edit.route)
                },
                onNavigateToGroups = {
                    navController.navigate(Destination.Group.Stack.route)
                }
            )
        }
        composable(route = Destination.Profile.Edit.route) {
            EditProfileScreen(
                onBackClick = {
                    navController.navigate(Destination.Profile.Home.route) {
                        popUpTo(Destination.Profile.Edit.route) { inclusive = true }
                    }
                }
            )
        }
    }
}