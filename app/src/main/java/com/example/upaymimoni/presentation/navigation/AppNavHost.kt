package com.example.upaymimoni.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.upaymimoni.presentation.navigation.graphs.authNavigationGraph
import com.example.upaymimoni.presentation.navigation.graphs.expenseAddGraph
import com.example.upaymimoni.presentation.navigation.graphs.expenseDetailGraph
import com.example.upaymimoni.presentation.navigation.graphs.groupNavigationGraph
import com.example.upaymimoni.presentation.navigation.graphs.initialLoadingGraph
import com.example.upaymimoni.presentation.navigation.graphs.profileNavigationGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destination.InitialLoading.route,
        modifier = modifier
    ) {
        initialLoadingGraph(navController)
        authNavigationGraph(navController)
        profileNavigationGraph(navController)
        groupNavigationGraph(navController)
        expenseAddGraph(navController)
        expenseDetailGraph(navController)
    }
}