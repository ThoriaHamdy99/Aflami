package com.amsterdam.ui.screens.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.loginScreenRoute(){
    composable<Route.Login> {
        LoginScreen()
    }
}