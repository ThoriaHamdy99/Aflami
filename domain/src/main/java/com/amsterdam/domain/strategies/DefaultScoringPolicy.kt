package com.amsterdam.domain.strategies

import com.amsterdam.entity.GameDifficulty.DifficultyType

class DefaultScoringPolicy : ScoringPolicy {
    
    override fun getPointsPerCorrect(difficulty: DifficultyType): Int = when (difficulty) {
        DifficultyType.EASY -> 5
        DifficultyType.MEDIUM -> 10
        DifficultyType.HARD -> 20
    }
    
    override fun getTimePerQuestion(difficulty: DifficultyType): Int = when (difficulty) {
        DifficultyType.EASY -> 45
        DifficultyType.MEDIUM -> 30
        DifficultyType.HARD -> 10
    }
    
    override fun getQuestionCount(difficulty: DifficultyType): Int = when (difficulty) {
        DifficultyType.EASY -> 5
        DifficultyType.MEDIUM -> 10
        DifficultyType.HARD -> 20
    }
}
