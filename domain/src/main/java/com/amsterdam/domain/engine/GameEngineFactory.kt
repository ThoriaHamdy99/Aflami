package com.amsterdam.domain.engine

import com.amsterdam.domain.strategies.QuestionProvider
import com.amsterdam.domain.strategies.HintStrategy
import com.amsterdam.domain.strategies.ScoringPolicy
import com.amsterdam.entity.Game.GameType

interface GameEngineFactory {
    fun createGameEngine(gameType: GameType): GameEngine
}

class DefaultGameEngineFactory(
    private val questionProviders: Map<GameType, QuestionProvider>,
    private val hintStrategies: Map<GameType, HintStrategy>,
    private val scoringPolicies: Map<GameType, ScoringPolicy>
) : GameEngineFactory {
    
    override fun createGameEngine(gameType: GameType): GameEngine {
        val questionProvider = questionProviders[gameType] 
            ?: throw IllegalArgumentException("No QuestionProvider found for game type: $gameType")
        
        val hintStrategy = hintStrategies[gameType] 
            ?: throw IllegalArgumentException("No HintStrategy found for game type: $gameType")
        
        val scoringPolicy = scoringPolicies[gameType] 
            ?: throw IllegalArgumentException("No ScoringPolicy found for game type: $gameType")
        
        return DefaultGameEngine(questionProvider, hintStrategy, scoringPolicy)
    }
}
