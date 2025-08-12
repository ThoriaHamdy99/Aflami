package com.amsterdam.viewmodel.game

import androidx.lifecycle.SavedStateHandle
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty

class GameArgs(
    savedStateHandle: SavedStateHandle,
) {
    val gameType =
        savedStateHandle.get<Game.GameType>("gameType") ?: Game.GameType.GUESS_MOVIE_BY_POSTER
    val difficulty = savedStateHandle.get<String>("difficulty")?.let { difficultyString ->
        try {
            GameDifficulty.DifficultyType.valueOf(difficultyString.uppercase())
        } catch (e: IllegalArgumentException) {
            GameDifficulty.DifficultyType.MEDIUM
        }
    } ?: GameDifficulty.DifficultyType.MEDIUM
}