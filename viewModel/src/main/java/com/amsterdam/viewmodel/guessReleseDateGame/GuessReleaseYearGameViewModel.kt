package com.amsterdam.viewmodel.guessReleseDateGame

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.releaseYear.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.domain.useCase.game.releaseYear.GuessReleaseYearGameUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuessReleaseYearGameViewModel @Inject constructor(
    private val guessReleaseYearForMovieGameUseCase: GuessReleaseYearGameUseCase,
    args: GuessReleaseYearGameArgs,
    dispatcherProvider: DispatcherProvider,
    private val timerHandler: TimerHandler
) : BaseViewModel<GuessReleaseYearUiState, GuessReleaseYearGameEffect>(
        GuessReleaseYearUiState(),
        dispatcherProvider
    ) {
    private val difficultyType = DifficultyType.valueOf(args.difficulty)

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::startTheGame,
            onSuccess = ::onSuccessGetQuestions,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun startTheGame(): List<MovieReleasedDateQuestion> {
        return guessReleaseYearForMovieGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<MovieReleasedDateQuestion>) {
        updateState { it.copy(questions = questions.toQuestionsUiStateUiState()) }
    }

    private fun onTimerUpdate(remainingSeconds: Int) {

    }

    private fun onTimeFinish() {

    }

    fun onError(error: Exception) {

    }

    fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }


}