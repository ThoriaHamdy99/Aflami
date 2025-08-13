package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.useCase.game.GetTotalUserPointsUseCase

class DoGuessGenreGameHintUseCase(
    private val getTotalUserPointsUseCase: GetUser
) {
    operator fun invoke(movieReleasedDateQuestion: GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion): GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion {
        val choices = movieReleasedDateQuestion.genreChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        return movieReleasedDateQuestion.copy(genreChoices = choices)
    }

    companion object {
        private const val REQUIRED_HINT_POINTS = 10
    }
}