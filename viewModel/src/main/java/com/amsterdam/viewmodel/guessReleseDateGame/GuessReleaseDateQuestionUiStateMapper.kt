package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.domain.useCase.game.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState.ChoiceUiState

fun MovieReleasedDateQuestion.toQuestionUiStateUiState(): QuestionUiState {
    return QuestionUiState(
        movieName = this.question,
        releaseYearChoices = this.releaseYearChoices.map { ChoiceUiState(it.toString()) }
    )
}

fun List<MovieReleasedDateQuestion>.toQuestionsUiStateUiState(): List<QuestionUiState>  = map { it.toQuestionUiStateUiState() }