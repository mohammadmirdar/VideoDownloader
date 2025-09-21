package com.mirdar.videodownloader.com.mirdar.videodownloader.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mirdar.designsystem.theme.VideoDownloaderTheme
import com.mirdar.videodownloader.com.mirdar.videodownloader.feature.history.HistoryScreen
import com.mirdar.videodownloader.feature.home.HomeScreen
import com.mirdar.videodownloader.feature.home.component.HomeBottomBar
import com.mirdar.videodownloader.feature.home.component.HomeTopBar
import com.mirdar.videodownloader.navigation.Screen

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val menuItems = remember { getMenus() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier,
        containerColor = VideoDownloaderTheme.colors.white,
        topBar = {
            HomeTopBar()
        }, bottomBar = {
            HomeBottomBar(
                menus = menuItems,
                currentDestination = navBackStackEntry?.destination,
                onNavigate = navController::navigate,
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            homeScreen(onNavigateToHistory = navController::navigateToHistory)

            historyScreen()
        }
    }
}

fun NavGraphBuilder.homeScreen(onNavigateToHistory: () -> Unit) {
    composable<Screen.Home> {
        HomeScreen(onNavigateToHistory = onNavigateToHistory)
    }
}

fun NavGraphBuilder.historyScreen() {
    composable<Screen.History> {
        HistoryScreen()
    }
}

fun NavController.navigateToHistory() {
    navigate(route = Screen.History)
}