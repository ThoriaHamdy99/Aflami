package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.GameDifficulty.DifficultyType

class GetGuessReleaseYearGameDataUseCase(
    private val generateMovieReleaseYearQuestions: GenerateMovieReleaseYearQuestionsUseCase,
    private val getDifficulty: GetGameDifficultyByDifficultyTypeUseCase
) {
    suspend operator fun invoke(difficultyType: DifficultyType): GuessReleaseYearGameData {
        val gameDifficulty = getDifficulty(difficultyType)
        val questions = generateMovieReleaseYearQuestions(gameDifficulty.totalQuestions)
        return GuessReleaseYearGameData(
            secondsPerQuestion = gameDifficulty.timeLimitSeconds,
            questions = questions
        )
    }

    data class GuessReleaseYearGameData(
        val secondsPerQuestion: Int,
        val questions: List<GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion>
    )
}
