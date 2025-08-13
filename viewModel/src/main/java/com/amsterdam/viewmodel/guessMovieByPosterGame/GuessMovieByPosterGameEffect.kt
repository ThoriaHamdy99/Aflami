package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.viewmodel.gameEnd.ResultScreenData
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearGameEffect

interface GuessMovieByPosterGameEffect {
    object NavigateBack : GuessMovieByPosterGameEffect
    data class NavigateToGameResult(val resultScreenData : ResultScreenData) :
        GuessMovieByPosterGameEffect
}