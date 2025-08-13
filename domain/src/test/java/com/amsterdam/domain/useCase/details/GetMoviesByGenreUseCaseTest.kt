package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieListWithCategories
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GetMoviesByGenreUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMoviesByGenreUseCase: GetMoviesByGenreUseCase


    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        getMoviesByGenreUseCase = GetMoviesByGenreUseCase(movieRepository)
    }

    @Test
    fun `invoke should call getMoviesByGenre from movieRepository`() = runTest {
        val selectedGenre = MovieGenre.ACTION
        val page = 1
        val movies = fakeMovieListWithCategories
        coEvery { movieRepository.getMoviesByGenre(selectedGenre, page) } returns movies

        val result = getMoviesByGenreUseCase(selectedGenre, page)

        assertThat(result).isEqualTo(movies)
        coVerify { movieRepository.getMoviesByGenre(selectedGenre, page) }
    }
}