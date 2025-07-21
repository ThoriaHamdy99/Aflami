package com.example.viewmodel.home

sealed interface HomeEffect {
    object NavigateToSearchScreenEffect : HomeEffect
    data class NavigateToMovieDetailsScreen(val movieId : Long) : HomeEffect
    object NavigateToTopRatedMoviesScreen : HomeEffect
}