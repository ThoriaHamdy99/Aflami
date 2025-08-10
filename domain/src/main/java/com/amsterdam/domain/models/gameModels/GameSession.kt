package com.amsterdam.domain.models.gameModels

data class GameSession(
    val id: String,
    val config: GameConfig,
    val questions: List<Question>,
    val currentIndex: Int,
    val score: Int,
    val hintUsedForQuestionIds: Set<String>,
    val removedOptionIdsByQuestion: Map<String, Set<String>>
)