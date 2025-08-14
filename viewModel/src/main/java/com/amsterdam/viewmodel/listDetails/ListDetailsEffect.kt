package com.amsterdam.viewmodel.listDetails

sealed interface ListDetailsEffect {
    data object NavigateBack : ListDetailsEffect
    data class NavigateToMovieDetailsScreen(val movieId: Long) : ListDetailsEffect
    data class NavigateToTvShowDetailsScreen(val tvShowId: Long): ListDetailsEffect
    data object ShowErrorSnackBar : ListDetailsEffect
    data object ShowDeletionSuccessSnackBar : ListDetailsEffect
    data object ShowRemoveMovieSuccessSnackBar : ListDetailsEffect
}