package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState.ChoiceUiState

interface GuessReleaseYearInteractionListener {
    fun onHintClicked()
    fun onSelectAnswer(selectedChoice : ChoiceUiState)
}