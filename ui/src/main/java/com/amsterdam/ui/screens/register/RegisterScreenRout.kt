package com.amsterdam.ui.screens.register


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.registerScreenRoute(){
    composable<Route.Register> {
        RegisterScreen()
    }
}
