package com.example.viewmodel.seriesDetails

import com.example.viewmodel.movieDetails.MovieDetailsEffect
import com.example.viewmodel.shared.BaseViewModel

sealed interface SeriesDetailsEffect : BaseViewModel.BaseUiEffect {
    object NavigateBack : SeriesDetailsEffect
    object NavigateToCastScreen : SeriesDetailsEffect
    object NavigateToLoginScreenEffect : SeriesDetailsEffect
}