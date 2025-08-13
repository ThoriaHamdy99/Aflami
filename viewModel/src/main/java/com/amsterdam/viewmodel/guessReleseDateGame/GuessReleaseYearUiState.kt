package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.viewmodel.sharedGame.TimerUiState

data class GuessReleaseYearUiState(
    val isLoading: Boolean = true,
    val timerCounter: Int = 0,
    val questions: List<QuestionUiState> = emptyList(),
    val selectedAnswer: String = "",
    val questionsCounts: Int = 0,
    val currentQuestionIndex: Int = 0,
    val timerUiState : TimerUiState = TimerUiState()
) {
    data class QuestionUiState(
        val movieName: String = "",
        val releaseYearChoices: List<String> = emptyList()
    )
}
