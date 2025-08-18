package com.amsterdam.viewmodel.guessMovieByPosterGame

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import kotlin.time.Duration.Companion.seconds

data class GuessMovieByPosterUiState(
    val isLoading: Boolean = true,
    val totalCollectedPoints: Int = 0,
    val gameSessionId : Long = 0,
    val earnedPoints : Int? = null,
    val questions: List<QuestionUiState> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect: Boolean? = null,
    val isHintEnabled: Boolean = true,
    val isNotEnoughPointsDialogVisible: Boolean = false,
    val isNextEnabled: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val timerUiState: TimerUiState = TimerUiState(),
    val isNetworkError : Boolean = false
) {
    data class QuestionUiState(
        val posterUrl: String = "",
        val movieNameChoices: List<String> = emptyList(),
        val correctAnswer: String = "",
        val questionTimeSeconds: Int = 0,
        val blurRadius: Int = 8
    )
}

fun GameQuestion<String>.toQuestionUiState(): GuessMovieByPosterUiState.QuestionUiState {
    return GuessMovieByPosterUiState.QuestionUiState(
        posterUrl = this.question,
        movieNameChoices = this.choices,
        correctAnswer = this.correctChoice,
        questionTimeSeconds = this.questionTime.inWholeSeconds.toInt()
    )
}

fun GuessMovieByPosterUiState.QuestionUiState.toMoviePosterQuestion(): GameQuestion<String> {
    return GameQuestion(
        question = this.posterUrl,
        choices = this.movieNameChoices,
        correctChoice = this.correctAnswer,
        questionTime = this.questionTimeSeconds.seconds
    )
}

fun List<GameQuestion<String>>.toQuestionsUiState(): List<GuessMovieByPosterUiState.QuestionUiState> =
    map { it.toQuestionUiState() }