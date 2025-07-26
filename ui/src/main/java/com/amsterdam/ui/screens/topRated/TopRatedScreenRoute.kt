package com.amsterdam.ui.screens.topRated

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.topRatedScreenRoute(){
    composable<Route.TopRated> {
        TopRatedScreen()
    }
}