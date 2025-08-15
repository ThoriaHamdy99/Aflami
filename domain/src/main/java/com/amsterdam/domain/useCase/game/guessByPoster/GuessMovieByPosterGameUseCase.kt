package com.amsterdam.domain.useCase.game.guessByPoster

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameQuestion

class GuessMovieByPosterGameUseCase(
    private val getGameData: GenerateMoviePosterQuestionsUseCase,
    private val doHint: DoGuessMovieByPosterHintUseCase,
    private val submitAnswer: SubmitGuessMovieByPosterAnswerUseCase
) {

    suspend fun startGame(difficultyType: GameDifficulty.DifficultyType) =
        getGameData(difficultyType)

    suspend fun giveHint(question: GameQuestion<String>) =
        doHint(question)

    suspend fun answer(
        question: GameQuestion<String>,
        answer: String,
        difficultyType: GameDifficulty.DifficultyType
    ) = submitAnswer(question, answer, difficultyType)
}
