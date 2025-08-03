package com.amsterdam.viewmodel.listDetails

interface ListDetailsEffect {
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ListDetailsEffect
    data class NavigateToTvShowDetailsEffect(val tvShowId: Long) : ListDetailsEffect
    object NavigateBack : ListDetailsEffect
}