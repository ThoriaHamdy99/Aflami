package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState.QuestionUiState
import kotlin.time.Duration.Companion.seconds

fun GameQuestion<Int>.toQuestionUiStateUiState(): QuestionUiState {
    return QuestionUiState(
        movieName = this.question,
        releaseYearAnswer = this.choices.map { it.toString() },
        correctAnswer = this.correctChoice.toString(),
        questionTimeSeconds = questionTime.inWholeSeconds.toInt()
    )
}

fun QuestionUiState.toMovieReleasedDateQuestion(): GameQuestion<Int> {
    return GameQuestion(
        question = this.movieName,
        choices = this.releaseYearAnswer.map { it.toInt() },
        correctChoice = this.correctAnswer.toInt(),
        questionTime = questionTimeSeconds.seconds
    )
}

fun List<GameQuestion<Int>>.toQuestionsUiStateUiState(): List<QuestionUiState> =
    map { it.toQuestionUiStateUiState() }