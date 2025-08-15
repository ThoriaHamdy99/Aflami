package com.amsterdam.domain.useCase.game.character

import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameQuestion

class GuessCharacterGameUseCase(
    private val getGameData: GenerateCharacterQuestionsUseCase,
    private val doHint: DoGuessCharacterGameHintUseCase,
    private val submitAnswer: SubmitCharacterAnswerUseCase
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