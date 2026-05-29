package com.scandoc.presentation.navigation

data class AppState(
    val route: AppRoute = AppRoute.Splash,
    val backStack: List<AppRoute> = listOf(AppRoute.Splash),
    val selectedTab: NavTab = NavTab.Home,
    val isCameraOverlayOpen: Boolean = false,
)

sealed interface AppEffect
