package com.amsterdam.viewmodel.guessWhichGenre

import com.amsterdam.domain.model.GameQuestion
import com.amsterdam.domain.model.category.MovieGenre

fun GameQuestionUiState.toQuestion() = GameQuestion(
    question = questionData,
    choices = answers,
    correctChoice = correctAnswer,
    questionTime = questionTime
)

fun GameQuestion<MovieGenre>.toUiState() = GameQuestionUiState(
    questionData = question,
    answers = choices,
    correctAnswer = correctChoice,
    questionTime = questionTime
)