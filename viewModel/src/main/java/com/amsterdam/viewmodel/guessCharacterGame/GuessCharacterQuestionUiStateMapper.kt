package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterUiState.CharacterQuestionUiState
import kotlin.time.Duration.Companion.seconds

fun GameQuestion<String>.toQuestionUiState(): CharacterQuestionUiState {
    return CharacterQuestionUiState(
        characterImageUrl = this.question,
        characterChoices = this.choices,
        correctAnswer = this.correctChoice,
        questionDurationSeconds = this.questionDuration.inWholeSeconds.toInt()
    )
}

fun CharacterQuestionUiState.toCharacterDataQuestion(): GameQuestion<String> {
    return GameQuestion(
        question = this.characterImageUrl,
        choices = this.characterChoices,
        correctChoice = this.correctAnswer,
        questionDuration = this.questionDurationSeconds.seconds
    )
}

fun List<GameQuestion<String>>.toQuestionsUiState(): List<CharacterQuestionUiState> =
    map { it.toQuestionUiState() }