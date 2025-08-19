package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.viewmodel.sharedGame.TimerUiState
import kotlin.time.Duration

data class GuessCharacterUiState(
    val isLoading: Boolean = true,
    val timerCounter: Int = 0,
    val gameSessionId : Long = 0,
    val totalCollectedPoints : Int = 0,
    val questions: List<CharacterQuestionUiState> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect : Boolean? = null,
    val isHintEnabled : Boolean = true,
    val isNotEnoughPointsDialogVisible: Boolean = false,
    val isNextEnabled : Boolean = false,
    val isNetworkError : Boolean = false,
    val earnedPoints : Int? = null,
    val questionsCounts: Int = 0,
    val currentQuestionIndex: Int = 0,
    val timerUiState : TimerUiState = TimerUiState(),
    val isRetryLoading : Boolean = false
) {
    data class CharacterQuestionUiState(
        val characterImageUrl: String = "",
        val characterChoices: List<String> = emptyList(),
        val correctAnswer : String = "",
        val questionDurationSeconds : Duration = Duration.ZERO,
        val blurRadius: Int = 8
    )
}


