package com.amsterdam.domain.strategies

import com.amsterdam.domain.models.gameModels.GameConfig
import com.amsterdam.domain.models.gameModels.GameSession
import com.amsterdam.domain.models.gameModels.Question
import com.amsterdam.entity.GameDifficulty.DifficultyType

interface QuestionProvider {
    suspend fun generateQuestions(config: GameConfig): List<Question>
}

interface HintStrategy {
    fun applyHint(session: GameSession): GameSession
    fun getHintCost(difficulty: DifficultyType): Int
}

interface ScoringPolicy {
    fun getPointsPerCorrect(difficulty: DifficultyType): Int
    fun getTimePerQuestion(difficulty: DifficultyType): Int
    fun getQuestionCount(difficulty: DifficultyType): Int
}
