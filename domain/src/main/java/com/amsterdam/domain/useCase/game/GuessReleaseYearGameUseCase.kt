package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.GameDifficulty.DifficultyType

class GuessReleaseYearGameUseCase(
    private val getGameData: GetGuessReleaseYearGameDataUseCase,
    private val doHint: DoGuessReleaseGameHintUseCase,
    private val submitAnswer: SubmitGuessReleaseYearAnswerUseCase
) {
    suspend fun startGame(difficultyType: DifficultyType) =
        getGameData(difficultyType)

    suspend fun giveHint(question: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion) =
        doHint(question)

    suspend fun answer(
        question: GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion,
        answer: Int,
        difficultyType: DifficultyType
    ) = submitAnswer(question, answer, difficultyType)
}

