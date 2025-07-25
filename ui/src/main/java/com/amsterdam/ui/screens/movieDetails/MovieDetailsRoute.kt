package com.amsterdam.ui.screens.movieDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.movieDetailsScreenRoute(){
    composable<Route.MovieDetails> {
        MovieDetailsScreen()
    }
}