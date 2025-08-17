package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.model.AnswerResult
import com.amsterdam.domain.model.GameQuestion
import com.amsterdam.domain.model.category.MovieGenre
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType

class SubmitGuessMovieGenreAnswerUseCase(
    private val getGameDifficultyUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val updateUserGamePointsUseCase: UpdateUserGamePointsUseCase
) {
    suspend operator fun invoke(
        question: GameQuestion<MovieGenre>,
        answer: MovieGenre,
        difficultyType: DifficultyType
    ): AnswerResult {
        val gameDifficulty = getGameDifficultyUseCase(difficultyType)
        val correct = question.correctChoice == answer
        var earnedPoints = 0
        if (question.correctChoice == answer){
            updateUserGamePointsUseCase(gameDifficulty.pointsPerQuestion)
            earnedPoints = gameDifficulty.pointsPerQuestion
        }

        return AnswerResult(correct, earnedPoints)
    }
}