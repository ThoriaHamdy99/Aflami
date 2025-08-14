package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.utils.fakeMovieListWithCategories
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class GetMoviesByGenreUseCaseTest {
    private val movieRepository: MovieRepository = mockk()
    private val getMoviesByGenreUseCase by lazy {
        GetMoviesByGenreUseCase(movieRepository)
    }

    @Test
    fun `when getMoviesByGenreUseCase is called, should call getMoviesByGenre from movieRepository`() =
        runTest {
            coEvery { movieRepository.getMoviesByGenre(selectedGenre, page) } returns movies

            val result = getMoviesByGenreUseCase(selectedGenre, page)

            assertThat(result).isEqualTo(movies)
        }

    @Test
    fun ` when getMoviesByGenreUseCase is called, should call getMoviesByGenre from movieRepository`() =
        runTest {
            coEvery { movieRepository.getMoviesByGenre(selectedGenre, page) } returns mockk()
            getMoviesByGenreUseCase(selectedGenre, page)

            coVerify(exactly = 1) { movieRepository.getMoviesByGenre(selectedGenre, page) }
        }

    private val selectedGenre = MovieGenre.ACTION
    private val page = 1
    private val movies = fakeMovieListWithCategories
}