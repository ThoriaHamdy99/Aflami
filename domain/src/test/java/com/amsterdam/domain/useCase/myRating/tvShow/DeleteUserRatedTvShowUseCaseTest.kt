package com.amsterdam.domain.useCase.myRating.tvShow

import com.amsterdam.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteUserRatedTvShowUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var deleteUserRatedTvShowUseCase: DeleteUserRatedTvShowUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk()
        deleteUserRatedTvShowUseCase = DeleteUserRatedTvShowUseCase(tvShowRepository)
    }

    @Test
    fun `deleteTvShowRate should call repository with correct ID`() = runTest {
        // Given
        val tvShowId = 123L
        coEvery { tvShowRepository.deleteTvShowRate(tvShowId) } just Runs

        // When
        val result = deleteUserRatedTvShowUseCase.deleteTvShowRate(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.deleteTvShowRate(tvShowId) }
        assertThat(result).isEqualTo(Unit)
    }
}
