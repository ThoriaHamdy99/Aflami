package com.amsterdam.viewmodel.sharedGame

interface BaseGameInteractionListener {
    fun startTheTimer(
        questionSecondsLimit: Int,
        onTimerChange: (BaseGameUiState) -> Unit,
        onTimerFinished: () -> Unit
    )

    fun stopTheTimer()
}