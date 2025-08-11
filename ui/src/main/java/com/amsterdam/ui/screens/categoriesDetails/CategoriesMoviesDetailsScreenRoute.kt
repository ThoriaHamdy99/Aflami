package com.amsterdam.ui.screens.categoriesDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.categoriesDetailsScreenRoute(){
    composable<Route.CategoriesDetails> {
        CategoriesMoviesDetailsScreen()
    }

}