package com.amsterdam.ui.screens.search.actorSearch

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.searchByActorScreenRoute(){
    composable<Route.SearchByActor> {
        SearchByActorScreen()
    }
}