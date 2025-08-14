package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.GameRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetUserPointsUseCaseTest {

    private val gamePointsRepository = mockk<GameRepository>()
    private val getUserPointsUseCase = GetUserPointsUseCase(gamePointsRepository)

    @Test
    fun `should call getPoints from repository`() = runTest {
        coEvery { gamePointsRepository.getUserPoints() } returns flowOf(40)

        getUserPointsUseCase.invoke()

        coVerify(exactly = 1) { gamePointsRepository.getUserPoints() }
    }

    @Test
    fun `should return the same points flow as repository`() = runTest {
        every { gamePointsRepository.getUserPoints() } returns flowOf(40)

        assertThat(getUserPointsUseCase().first()).isEqualTo(40)
    }

}