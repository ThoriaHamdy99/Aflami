package com.amsterdam.domain.useCase

import com.amsterdam.domain.engine.GameEngine
import com.amsterdam.domain.models.gameModels.GameConfig


class StartGameSessionUseCase(
    private val gameEngine: GameEngine
) {
    suspend operator fun invoke(config: GameConfig) {
        gameEngine.start(config)
    }
}
