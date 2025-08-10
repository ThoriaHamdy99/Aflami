package com.amsterdam.domain.strategies

import com.amsterdam.domain.models.gameModels.GameSession
import com.amsterdam.entity.GameDifficulty.DifficultyType

class DefaultHintStrategy : HintStrategy {
    
    override fun getHintCost(difficulty: DifficultyType): Int = when (difficulty) {
        DifficultyType.EASY -> 2
        DifficultyType.MEDIUM -> 4
        DifficultyType.HARD -> 6
    }
    
    override fun applyHint(session: GameSession): GameSession {
        val hintCost = getHintCost(session.config.difficulty)

        return session.copy(
            score = session.score - hintCost,
            hintUsedForQuestionIds = session.hintUsedForQuestionIds
        )
    }
}
