package com.amsterdam.domain.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

class TimerHandler {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var timerJob: Job? = null
    private var endTimeMillis: Long = 0L
    private var isTimerStopped = false

    fun startTimer(
        totalSeconds: Int,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimerFinish: () -> Unit
    ) {
        timerJob?.cancel()
        isTimerStopped = false
        endTimeMillis = System.currentTimeMillis() + totalSeconds * 1000L

        timerJob = scope.launch {
            while (true) {
                val remainingMillis = endTimeMillis - System.currentTimeMillis()
                val remainingSeconds = max(0, (remainingMillis / 1000).toInt())

                onTimerUpdate(remainingSeconds)

                if (remainingSeconds <= 0 || isTimerStopped) {
                    onTimerFinish()
                    break
                }
                delay(1000L)
            }
        }
    }

    fun stopTimer() {
        isTimerStopped = true
        timerJob?.cancel()
    }

}