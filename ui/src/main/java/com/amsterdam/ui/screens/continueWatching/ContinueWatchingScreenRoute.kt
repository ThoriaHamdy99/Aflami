package com.amsterdam.ui.screens.continueWatching

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.continueWatchingScreenRoute(){
    composable<Route.ContinueWatching> {
        ContinueWatchingScreen()
    }
}