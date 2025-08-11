package com.amsterdam.viewmodel.game

import com.amsterdam.entity.Game

data class GameUiState(
    val gameType: Game.GameType = Game.GameType.GUESS_CHARACTER,
    val questions: List<GameQuestionUiState> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val timer: String = "",
)

data class GameQuestionUiState(
    val name: String,
    val answers: List<String>,
)
