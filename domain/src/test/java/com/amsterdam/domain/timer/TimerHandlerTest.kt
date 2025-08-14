package com.amsterdam.domain.timer

import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TimerHandlerTest {

    @Test
    fun `timer starts with actual correct seconds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        assertThat(flow.value).isEqualTo(startTime)
    }

    @Test
    fun `remaining time value goes down by one when one second passes`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        advanceTimeBy(1000)
        runCurrent()

        assertThat(flow.value).isEqualTo(4)
    }

    @Test
    fun `timer counts down to zero`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        val flow = handler.startTimer(totalSeconds = startTime) {}

        advanceTimeBy(5000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `timer calls onTimerFinish when it reaches zero`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        handler.startTimer(totalSeconds = startTime) { finishCalls++ }

        advanceTimeBy(5000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(1)
    }

    @Test
    fun `timer does not call onTimerFinish early`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val startTime = 5

        handler.startTimer(totalSeconds = startTime) { finishCalls++ }

        advanceTimeBy(3000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(0)
    }

    @Test
    fun `starting a new timer early cancels old one`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(3000)
        runCurrent()

        handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(7)
    }

    @Test
    fun `starting a new timer early cancels old one and correctly starts new one`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(3000)
        runCurrent()

        val flow = handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(2)
    }

    @Test
    fun `starting a new timer early does not call onTimerFinish`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        var finishCalls = 0
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        handler.startTimer(totalSeconds = 10) { finishCalls++ }

        advanceTimeBy(3000)
        runCurrent()

        handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(3000)
        runCurrent()

        assertThat(finishCalls).isEqualTo(0)
    }

    @Test
    fun `timer handles exact zero case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = 0) {}

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `timer handles negative start seconds case`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }

        val flow = handler.startTimer(totalSeconds = -5) {}

        advanceTimeBy(1000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `stopTimer cancels timer early`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val flow = handler.startTimer(totalSeconds = 5) {}

        advanceTimeBy(2000)
        runCurrent()

        handler.stopTimer()
        advanceTimeBy(3000)
        runCurrent()

        assertThat(flow.value).isEqualTo(3)
    }

    @Test
    fun `timer works with default nowMillis and dispatcher`() {
        val handler = TimerHandler()
        val flow = handler.startTimer(totalSeconds = 1) {}
        assertThat(flow.value).isEqualTo(1)
    }

    @Test
    fun `timer handles large jumps in wall clock time`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val flow = handler.startTimer(totalSeconds = 10) {}

        advanceTimeBy(15_000)
        runCurrent()

        assertThat(flow.value).isEqualTo(0)
    }

    @Test
    fun `stopTimer does nothing when timerJob is null`() = runTest{
        val dispatcher = StandardTestDispatcher(testScheduler)
        val handler = TimerHandler(dispatcher) { testScheduler.currentTime }
        val mockJob = mockk<Job>()

        handler.stopTimer()

        coVerify(exactly = 0) { mockJob.cancel(any()) }
    }
}
