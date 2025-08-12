package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.domain.useCase.game.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState

fun MovieReleasedDateQuestion.toQuestionUiStateUiState(): QuestionUiState {
    return QuestionUiState(
        movieName = this.question,
        releaseYearChoices = this.releaseYearChoices.map { it.toString() }
    )
}

fun List<MovieReleasedDateQuestion>.toQuestionsUiStateUiState(): List<QuestionUiState>  = map { it.toQuestionUiStateUiState() }