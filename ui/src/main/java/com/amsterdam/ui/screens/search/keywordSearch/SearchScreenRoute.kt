package com.amsterdam.ui.screens.search.keywordSearch

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

internal fun NavGraphBuilder.searchScreenRoute(){
    composable<Route.Search> {
        SearchScreen()
    }
}