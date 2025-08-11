package com.amsterdam.viewmodel.game

import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel
    @Inject
    constructor(
        dispatcherProvider: DispatcherProvider,
    ) : BaseViewModel<GameUiState, GameEffect>(
            GameUiState(),
            dispatcherProvider,
        ),
        GameInteractionListener {
        override fun onCancelGameClick() {
            TODO("Not yet implemented")
        }

        override fun onChooseAnswerClick(answer: String) {
            TODO("Not yet implemented")
        }
    }
