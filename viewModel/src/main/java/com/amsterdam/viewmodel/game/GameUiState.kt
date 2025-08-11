package com.amsterdam.viewmodel.game

data class GameUiState(
    val questions: List<GameQuestionUiState> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val timer: String = "",
)

data class GameQuestionUiState(
    val name: String,
    val answers: List<String>,
)
