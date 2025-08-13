package com.amsterdam.viewmodel.gameEnd

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.GameDifficulty

class GameResultArgs(savedStateHandle: SavedStateHandle) {
    val totalCollectedPoints: Int? = savedStateHandle.get<Int>(TOTAL_COLLECTED_POINTS_ARGS)
    val totalSpentSeconds: Int? = savedStateHandle.get<Int>(TOTAL_SPENT_SECONDS_ARGS)

    val gameType: ResultSideEffect.GameType? = savedStateHandle.get<String>(GAME_TYPE_ARGS)?.let {
        ResultSideEffect.GameType.valueOf(it)
    }

    val gameDifficulty: GameDifficulty.DifficultyType? = savedStateHandle.get<String>(GAME_DIFFICULTY_ARGS)?.let {
        GameDifficulty.DifficultyType.valueOf(it)
    }

    companion object {
        const val TOTAL_COLLECTED_POINTS_ARGS = "totalCollectedPoints"
        const val TOTAL_SPENT_SECONDS_ARGS = "totalSpentSeconds"
        const val GAME_TYPE_ARGS = "gameType"
        const val GAME_DIFFICULTY_ARGS = "gameDifficulty"
    }
}