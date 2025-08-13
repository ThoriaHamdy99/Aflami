package com.amsterdam.viewmodel.guessMovieByPosterGame

interface GuessMovieByPosterGameEffect {
    object NavigateBack : GuessMovieByPosterGameEffect
    data class NavigateToGameResult(val totalCollectedPoints: Int, val totalSpentSeconds: Int) :
        GuessMovieByPosterGameEffect
}