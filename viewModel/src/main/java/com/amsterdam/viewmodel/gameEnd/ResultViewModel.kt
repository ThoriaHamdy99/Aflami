package com.amsterdam.viewmodel.gameEnd

import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    gameResultArgs: GameResultArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<ResultUiState, ResultSideEffect>(
    initialState = ResultUiState(),
    dispatcherProvider = dispatcherProvider
), ResultInteractionListener {

    private val gameType = gameResultArgs.gameType!!
    private val difficulty = gameResultArgs.gameDifficulty!!

    init {
        val points = gameResultArgs.totalCollectedPoints!!
        val time = gameResultArgs.totalSpentSeconds!!

        updateState {
            it.copy(points = points, timeInSeconds = time)
        }
    }

    override fun onClickPlayAgain() {
        sendNewNavigationEffect(
            ResultSideEffect.NavigateToGame(
                gameType = gameType,
                difficulty = difficulty,
                totalCollectedPoints = 0,
                totalSpentSeconds = 0
            )
        )
    }

    override fun onClickBackToMenu() {
        sendNewNavigationEffect(ResultSideEffect.NavigateBackToMenu)
    }

    override fun onClickClose() {
        onClickBackToMenu()
    }
}