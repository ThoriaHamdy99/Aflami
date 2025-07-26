package com.amsterdam.ui.screens.cast

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.castScreenRoute(){
    composable<Route.Cast> {
        CastScreen()
    }
}