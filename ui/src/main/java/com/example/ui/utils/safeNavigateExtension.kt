package com.example.ui.utils

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.example.ui.navigation.Route

fun NavController.safeNavigate(
    route: Route,
    builder: (NavOptionsBuilder.() -> Unit)? = {
        launchSingleTop = true
    }
) {
    this.navigate(route, builder ?: {})
}

fun NavController.safeNavigate(
    route: Any,
    builder: (NavOptionsBuilder.() -> Unit)? = {
        launchSingleTop = true
    }
) {
    this.navigate(route, builder ?: {})
}
