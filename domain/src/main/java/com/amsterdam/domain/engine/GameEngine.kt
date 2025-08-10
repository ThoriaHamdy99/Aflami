package com.amsterdam.domain.engine

import com.amsterdam.domain.models.gameModels.GameConfig
import com.amsterdam.domain.models.gameModels.GameSessionState
import kotlinx.coroutines.flow.StateFlow

interface GameEngine {
    val state: StateFlow<GameSessionState>
    
    suspend fun start(config: GameConfig)
    fun submitAnswer(optionId: String)
    fun useHint()
    fun skip()
    fun next()
    fun tick(deltaMillis: Long)
    fun finish()
    fun reset()
}
