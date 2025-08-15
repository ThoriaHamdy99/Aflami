package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameQuestion

class SubmitCharacterAnswerUseCase(
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: GameQuestion<String>,
        answer: String,
        difficultyType: GameDifficulty.DifficultyType
    ): AnswerResult {
        val gameDifficulty = getDifficulty(difficultyType)
        val correct = question.correctChoice == answer
        var earnedPoints = 0
        if (correct) {
            updatePoints(gameDifficulty.pointsPerQuestion)
            earnedPoints = gameDifficulty.pointsPerQuestion
        }

        return AnswerResult(correct, earnedPoints)
    }

    data class AnswerResult(
        val isCorrect: Boolean,
        val earnedPoints: Int
    )
}