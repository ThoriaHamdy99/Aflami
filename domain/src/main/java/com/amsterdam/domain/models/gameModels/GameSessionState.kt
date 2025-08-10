package com.amsterdam.domain.models.gameModels


data class GameSessionState(
    val status: GameStatus,
    val session: GameSession?,
    val timeLeftSeconds: Int,
    val currentQuestion: Question?,
    val options: List<AnswerOption>,
    val hintState: HintState,
    val selectedOptionId: String? = null,
    val isCorrect: Boolean? = null,
    val pointsEarned: Int = 0
)