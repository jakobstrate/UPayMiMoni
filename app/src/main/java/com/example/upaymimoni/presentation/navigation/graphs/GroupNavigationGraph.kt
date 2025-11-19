package com.example.upaymimoni.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.upaymimoni.presentation.navigation.Destination
import com.example.upaymimoni.presentation.ui.ExpenseDetailScreen
import com.example.upaymimoni.presentation.ui.groups.GroupScreen
import com.example.upaymimoni.presentation.ui.groups.GroupsScreen
import com.example.upaymimoni.presentation.ui.groups.EditGroupScreen


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
            GroupScreen(
                onNavigateToEditGroup = {
                    navController.navigate(Destination.Group.Edit.route)
                }
            )
        }

        composable(
            route = Destination.Group.Edit.route,
            arguments = listOf(
                navArgument(Destination.Group.Edit.ARG_GROUP_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString(
                Destination.Group.Edit.ARG_GROUP_ID
            ) ?: error("groupId is required")

            EditGroupScreen(
                groupId = groupId,
                onBackClick = { navController.navigateUp() },
                onSaveClick = {}
            )
        }
    }
}