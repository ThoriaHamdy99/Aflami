package com.amsterdam.viewmodel.myRating

sealed interface MyRatingUiEffect {
    object NavigateBack : MyRatingUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : MyRatingUiEffect
    data class NavigateToSeriesDetails(val tvShowId: Long) : MyRatingUiEffect

    object ShowDeleteRateSuccessSnackBar: MyRatingUiEffect
    object ShowDeleteRateErrorSnackBar: MyRatingUiEffect
}