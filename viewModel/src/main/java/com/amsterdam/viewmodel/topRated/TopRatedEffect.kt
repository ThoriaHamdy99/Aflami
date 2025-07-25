package com.amsterdam.viewmodel.topRated

interface TopRatedEffect {
    data class NavigateToMovieDetailsScreen(val movieId : Long) : TopRatedEffect
    object NavigateBack : TopRatedEffect
}