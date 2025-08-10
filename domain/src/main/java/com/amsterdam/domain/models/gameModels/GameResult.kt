package com.amsterdam.domain.models.gameModels

data class GameResult(
    val sessionId: String,
    val score: Int,
    val totalTimeSpent: Int,
    val questionsAnswered: Int,
    val correctAnswers: Int
) 
