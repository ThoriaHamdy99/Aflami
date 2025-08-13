package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.useCase.game.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion

class DoGuessReleaseGameHintUseCase {

    operator fun invoke(movieReleasedDateQuestion: MovieReleasedDateQuestion) : MovieReleasedDateQuestion  {
        val choices = movieReleasedDateQuestion.releaseYearChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        return movieReleasedDateQuestion.copy(releaseYearChoices = choices)
    }

}