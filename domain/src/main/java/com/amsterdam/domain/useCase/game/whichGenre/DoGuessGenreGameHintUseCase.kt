package com.amsterdam.domain.useCase.game.whichGenre

class DoGuessGenreGameHintUseCase {
    operator fun invoke(movieReleasedDateQuestion: GetGenreQuestionsUseCase.MovieGenreQuestion): GetGenreQuestionsUseCase.MovieGenreQuestion {
        val choices = movieReleasedDateQuestion.genreChoices.toMutableList()

        val wrongChoiceToRemove = choices
            .filter { it != movieReleasedDateQuestion.correctChoice }
            .random()

        choices.remove(wrongChoiceToRemove)
        return movieReleasedDateQuestion.copy(genreChoices = choices)
    }
}