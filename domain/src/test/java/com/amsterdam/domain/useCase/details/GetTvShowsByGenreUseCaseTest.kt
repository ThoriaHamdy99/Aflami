package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.utils.fakeTvShowListWithCategories
import com.amsterdam.entity.category.TvShowGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetTvShowsByGenreUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk()
        getTvShowsByGenreUseCase = GetTvShowsByGenreUseCase(tvShowRepository)
    }

    @Test
    fun `invoke should call getTvShowsByGenre from tvShowRepository`() = runTest {
        val selectedGenre = TvShowGenre.COMEDY
        val page = 1
        val tvShows = fakeTvShowListWithCategories
        coEvery { tvShowRepository.getTvShowsByGenre(selectedGenre, page) } returns tvShows
        val result = getTvShowsByGenreUseCase(selectedGenre, page)
        assertThat(result).isEqualTo(tvShows)
        coVerify { tvShowRepository.getTvShowsByGenre(selectedGenre, page) }

    }
}