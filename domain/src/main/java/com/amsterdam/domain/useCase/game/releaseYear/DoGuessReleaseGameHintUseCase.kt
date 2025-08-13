package com.amsterdam.domain.useCase.game.releaseYear

import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase

class DoGuessReleaseGameHintUseCase(
    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase
) {

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }

    suspend operator fun invoke(
        movieReleasedDateQuestion: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
    ): GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion {

        val currentPoints = getTotalUserPointsUseCase()

        if (currentPoints < REQUIRED_HINT_POINTS) {
            return movieReleasedDateQuestion
        }

        val choices = movieReleasedDateQuestion.releaseYearChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)

        return movieReleasedDateQuestion.copy(releaseYearChoices = choices)
    }
}
