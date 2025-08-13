package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.GameDifficulty.DifficultyType

class SubmitGuessReleaseYearAnswerUseCase(
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion,
        answer: Int,
        difficultyType: DifficultyType
    ): AnswerResult {
        val gameDifficulty = getDifficulty(difficultyType)
        val correct = question.correctChoice == answer
        if (correct) {
            updatePoints(gameDifficulty.pointsPerQuestion)
        }
        return AnswerResult(correct, gameDifficulty.pointsPerQuestion)
    }

    data class AnswerResult(
        val isCorrect: Boolean,
        val earnedPoints: Int
    )
}
