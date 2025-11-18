package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.groups.GroupScreen
import com.example.upaymimoni.presentation.ui.groups.GroupsScreen

fun NavGraphBuilder.groupNavigationGraph(navController: NavHostController) {
    navigation(
        startDestination = Destination.Group.Overview.route,
        route = Destination.Group.Stack.route
    ) {
        composable(route = Destination.Group.Overview.route) {
            GroupsScreen(
                onNavigateToProfile = {
                    navController.navigate(Destination.Profile.Stack.route)
                }
            )
        }

        composable(route = Destination.Group.Instance.route) {
            GroupScreen()
        }
    }
}