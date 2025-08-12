package com.amsterdam.ui.screens.games

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.guessReleaseYearScreenScreenRoute(){
    composable<Route.Login> {
        GuessReleaseYearScreen()
    }
}