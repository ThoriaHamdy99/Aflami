package com.amsterdam.domain.useCase.game.whichGenre

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty

class GuessMovieGenreGameEngin(
    private val getGenreQuestionsUseCase: GetGenreQuestionsUseCase,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val doGuessGenreGameHintUseCase: DoGuessGenreGameHintUseCase,
    private val timerHandler: TimerHandler
) {
    val cachedQuestions: MutableList<GetGenreQuestionsUseCase.MovieGenreQuestion> = mutableListOf()

    suspend fun startGame(
        difficultyType: GameDifficulty.DifficultyType,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimeFinish: () -> Unit
    ): List<GetGenreQuestionsUseCase.MovieGenreQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val questions = getGenreQuestionsUseCase(
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

    fun useHint(questionId: Long): GetGenreQuestionsUseCase.MovieGenreQuestion {
        val question = cachedQuestions.find { it.id == questionId }
        return doGuessGenreGameHintUseCase(question!!)
    }

    fun checkUserAnswer(answer: String, questionId: Long): Boolean {
        val question = cachedQuestions.find { it.id == questionId }
        val isCorrect = question?.correctChoice?.name == answer
        return isCorrect
    }
}
