package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.utils.fakeTvShowListWithCategories
import com.amsterdam.entity.category.TvShowGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class GetTvShowsByGenreUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk()
    private val getTvShowsByGenreUseCase by lazy {
        GetTvShowsByGenreUseCase(tvShowRepository)
    }

    @Test
    fun `invoke should call getTvShowsByGenre from tvShowRepository`() = runTest {
        coEvery { tvShowRepository.getTvShowsByGenre(selectedGenre, page) } returns tvShows
        val result = getTvShowsByGenreUseCase(selectedGenre, page)
        assertThat(result).isEqualTo(tvShows)
    }
    @Test
    fun ` should call getTvShowsByGenre from tvShowRepository`() = runTest {
        coEvery { tvShowRepository.getTvShowsByGenre(selectedGenre, page) } returns mockk()
         getTvShowsByGenreUseCase(selectedGenre, page)
        coVerify { tvShowRepository.getTvShowsByGenre(selectedGenre, page) }

    }
    val selectedGenre = TvShowGenre.COMEDY
    val page = 1
    val tvShows = fakeTvShowListWithCategories
}