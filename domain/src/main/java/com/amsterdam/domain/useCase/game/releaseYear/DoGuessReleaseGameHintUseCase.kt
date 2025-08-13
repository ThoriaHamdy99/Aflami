package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.exceptions.NoEnoughPointsException
import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.UpdateUserGamePointsUseCase

class DoGuessReleaseGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase,
    private val updatePoints: UpdateUserGamePointsUseCase
) {

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }

    suspend operator fun invoke(
        movieReleasedDateQuestion: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
    ): GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion {

        val currentPoints = getTotalUserPointsUseCase()

        if (currentPoints < REQUIRED_HINT_POINTS)
            throw NoEnoughPointsException()

        val choices = movieReleasedDateQuestion.releaseYearChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        updatePoints(-REQUIRED_HINT_POINTS)
        return movieReleasedDateQuestion.copy(releaseYearChoices = choices)
    }
}
