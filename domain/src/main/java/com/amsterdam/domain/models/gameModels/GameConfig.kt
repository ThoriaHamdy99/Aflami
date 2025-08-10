package com.amsterdam.domain.models.gameModels

import com.amsterdam.entity.Game.GameType
import com.amsterdam.entity.GameDifficulty.DifficultyType

data class GameConfig(
    val type: GameType,
    val difficulty: DifficultyType
)