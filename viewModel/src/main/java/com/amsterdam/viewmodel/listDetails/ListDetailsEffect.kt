package com.amsterdam.viewmodel.listDetails

interface ListDetailsEffect {
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ListDetailsEffect
    object NavigateBack : ListDetailsEffect
}