package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.viewmodel.sharedGame.GameResultUiState

interface GuessMovieByPosterGameEffect {
    object NavigateBack : GuessMovieByPosterGameEffect
    data class NavigateToGameResult(val gameResult: GameResultUiState) : GuessMovieByPosterGameEffect
}