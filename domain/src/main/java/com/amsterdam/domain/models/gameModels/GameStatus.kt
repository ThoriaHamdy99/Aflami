package com.amsterdam.domain.models.gameModels

sealed class GameStatus {
    object Idle : GameStatus()
    object Loading : GameStatus()
    object Active : GameStatus()
    object QuestionAnswered : GameStatus()
    object Finished : GameStatus()
}