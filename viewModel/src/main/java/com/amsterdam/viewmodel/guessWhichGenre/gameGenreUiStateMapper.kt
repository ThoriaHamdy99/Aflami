package com.amsterdam.viewmodel.guessWhichGenre

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.category.MovieGenre

fun GameQuestionUiState.toQuestion() = GameQuestion(
    question = questionData,
    choices = answers,
    correctChoice = correctAnswer,
    questionDuration = questionDuration
)

fun GameQuestion<MovieGenre>.toUiState() = GameQuestionUiState(
    questionData = question,
    answers = choices,
    correctAnswer = correctChoice,
    questionDuration = questionDuration
)