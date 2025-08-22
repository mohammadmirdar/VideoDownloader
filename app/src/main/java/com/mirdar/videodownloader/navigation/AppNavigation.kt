package com.mirdar.videodownloader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mirdar.videodownloader.feature.home.HomeScreen

@Composable
fun AppNavigation(
    onBackClick: () -> Unit,
    startDestination: Screen = Screen.Home,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        home()
    }
}

fun NavGraphBuilder.home() {
    composable<Screen.Home> {
        HomeScreen()
    }
}