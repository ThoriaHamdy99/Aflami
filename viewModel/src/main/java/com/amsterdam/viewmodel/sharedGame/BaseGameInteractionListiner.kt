package com.amsterdam.viewmodel.sharedGame

interface BaseGameInteractionListener {
    fun startTheTimer(
        questionTimeLimitSeconds: Int,
        onTimerChange: (BaseGameUiState) -> Unit,
        onTimerFinished: () -> Unit
    )

    fun stopTheTimer()
}