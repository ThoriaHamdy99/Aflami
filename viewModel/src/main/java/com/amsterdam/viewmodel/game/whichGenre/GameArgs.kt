package com.amsterdam.viewmodel.game.whichGenre

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.GameDifficulty

class GameArgs(
    savedStateHandle: SavedStateHandle,
) {
    val difficulty = savedStateHandle.get<String>("difficulty")?.let { difficultyString ->
        try {
            GameDifficulty.DifficultyType.valueOf(difficultyString.uppercase())
        } catch (e: IllegalArgumentException) {
            GameDifficulty.DifficultyType.MEDIUM
        }
    } ?: GameDifficulty.DifficultyType.MEDIUM
}