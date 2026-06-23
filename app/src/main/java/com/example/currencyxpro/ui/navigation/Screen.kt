package com.example.currencyxpro.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Rates : Screen("rates")
    object Analytics : Screen("analytics")
    object Favorites : Screen("favorites")
    object History : Screen("history")
    object Settings : Screen("settings")
}
