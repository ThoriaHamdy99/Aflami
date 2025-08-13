package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.entity.GameDifficulty

class GuessReleaseYearForMovieGameEngine(
    private val generateMovieReleaseYearQuestionsUseCase: GenerateMovieReleaseYearQuestionsUseCase,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val doGuessReleaseGameHintUseCase: DoGuessReleaseGameHintUseCase,
    private val timerHandler: TimerHandler
) {

    suspend fun startGame(
        difficultyType: GameDifficulty.DifficultyType,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimeFinish: () -> Unit
    ): List<MovieReleasedDateQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val questions = generateMovieReleaseYearQuestionsUseCase(
            questionCount = gameDifficulty.totalQuestions
        )

        timerHandler.startTimer(
            totalSeconds = gameDifficulty.timeLimitSeconds,
            onTimerUpdate = onTimerUpdate,
            onTimerFinish = onTimeFinish
        )
        return questions
    }

    data class GuessReleaseYearGame(
        val hint: Int,
        val questions: List<MovieReleasedDateQuestion>
    )

    fun useHint(movieReleasedDateQuestion: MovieReleasedDateQuestion) {
        doGuessReleaseGameHintUseCase
    }

    //fun submitA
}
