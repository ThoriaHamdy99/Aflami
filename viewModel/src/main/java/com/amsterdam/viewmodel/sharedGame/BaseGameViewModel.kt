package com.amsterdam.viewmodel.sharedGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.BaseGameUiState.TimerColor
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

open class BaseGameViewModel<S, E>(
    initialState: S,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel<S, E>(initialState, dispatcherProvider), BaseGameInteractionListener {

    private var timerJob: Job? = null
    private var endTimeMillis: Long = 0L
    private var isStopTimerClicked: Boolean = false

    override fun startTheTimer(
        questionTimeLimitSeconds: Int,
        onTimerChange: (BaseGameUiState) -> Unit,
        onTimerFinished: () -> Unit
    ) {
        timerJob?.cancel()
        isStopTimerClicked = false
        endTimeMillis = System.currentTimeMillis() + (questionTimeLimitSeconds * 1000L)

        timerJob = viewModelScope.launch(dispatcherProvider.Default) {
            while (true) {
                val remainingMillis = endTimeMillis - System.currentTimeMillis()
                val remainingSeconds = max(0, (remainingMillis / 1000).toInt())
                val progress = remainingSeconds / (questionTimeLimitSeconds.toFloat())
                onTimerChange(
                    BaseGameUiState(
                        remainingSeconds,
                        getCurrentTimerColor(remainingSeconds),
                        progress
                    )
                )

                if (remainingSeconds <= 0 || isStopTimerClicked) {
                    onTimerFinished()
                    break
                }
                delay(1000)
            }
        }
    }


    override fun stopTheTimer() {
        isStopTimerClicked = true
    }

    private fun getCurrentTimerColor(currentTimerCount: Int): TimerColor {
        return if (currentTimerCount > 5) TimerColor.GREEN else TimerColor.RED
    }
}