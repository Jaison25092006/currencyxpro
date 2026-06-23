package com.example.currencyxpro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.currencyxpro.ui.components.AppBottomNavigation
import com.example.currencyxpro.ui.components.NavigationItem
import com.example.currencyxpro.ui.screens.analytics.AnalyticsScreen
import com.example.currencyxpro.ui.screens.favorites.FavoritesScreen
import com.example.currencyxpro.ui.screens.history.HistoryScreen
import com.example.currencyxpro.ui.screens.home.HomeScreen
import com.example.currencyxpro.ui.screens.rates.RatesScreen
import com.example.currencyxpro.ui.screens.settings.SettingsScreen
import com.example.currencyxpro.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavigationItems = listOf(
        NavigationItem("Convert", Icons.Default.SwapVert, Screen.Home),
        NavigationItem("Rates", Icons.Default.TrendingUp, Screen.Rates),
        NavigationItem("Trends", Icons.Default.ShowChart, Screen.Analytics),
        NavigationItem("Favorites", Icons.Default.Star, Screen.Favorites),
        NavigationItem("History", Icons.Default.History, Screen.History),
        NavigationItem("Settings", Icons.Default.Settings, Screen.Settings)
    )

    val showBottomBar = currentRoute != Screen.Splash.route && currentRoute != null

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigation(navController = navController, items = bottomNavigationItems)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onInitializationComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Rates.route) {
                RatesScreen()
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen()
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
