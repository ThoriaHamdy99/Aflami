package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.viewmodel.sharedGame.GameResultUiState


interface GuessReleaseYearGameEffect {
    object NavigateBack : GuessReleaseYearGameEffect
    data class NavigateToGameResult(val gameResult: GameResultUiState) : GuessReleaseYearGameEffect
}