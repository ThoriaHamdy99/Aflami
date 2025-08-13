package com.amsterdam.viewmodel.gameEnd


interface GameResultArgs {
    val totalCollectedPoints: Int
    val totalSpentSeconds: Int,
    val gameType: ResultSideEffect.GameType,
    val gameDifficulty: GameDifficulty.DifficultyType
}