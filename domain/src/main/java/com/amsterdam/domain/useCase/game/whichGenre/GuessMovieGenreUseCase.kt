package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty

class GuessMovieGenreUseCase(
    private val generateMovieGenreQuestionsUseCase: GenerateMovieGenreQuestionsUseCase,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase,
    private val timerHandler: TimerHandler
) {
    val cachedQuestions: MutableList<GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion> = mutableListOf()

    suspend fun startGame(
        difficultyType: GameDifficulty.DifficultyType,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimeFinish: () -> Unit
    ): List<GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val questions = generateMovieGenreQuestionsUseCase(
            questionCount = gameDifficulty.totalQuestions
        )
        cachedQuestions.addAll(questions)

        timerHandler.startTimer(
            totalSeconds = gameDifficulty.timeLimitSeconds,
            onTimerUpdate = onTimerUpdate,
            onTimerFinish = onTimeFinish
        )
        return questions
    }

    fun useHint(questionId: Long): GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion {
        val question = cachedQuestions.find { it.id == questionId }
        return doGuessGenreGameHintUseCase(question!!)
    }

    fun checkUserAnswer(answer: String, questionId: Long): Boolean {
        val question = cachedQuestions.find { it.id == questionId }
        val isCorrect = question?.correctChoice?.name == answer
        return isCorrect
    }
}
