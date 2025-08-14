package com.amsterdam.domain.useCase.myRating.movie


import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.entity.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetUserRatedMoviesUseCaseTest {

    private val movieRepository: MovieRepository = mockk()
    private val useCase by lazy {
        GetUserRatedMoviesUseCase(movieRepository)
    }

    @Test
    fun `should return sorted list by user rate descending when invoked`() = runTest {
        val unsortedRatedMovies = listOf(
            UserRatedMovie(movie = fakeMovieList.first(), userRate = 5),
            UserRatedMovie(movie = fakeMovieList.last(), userRate = 9)
        )

        coEvery { movieRepository.getUserRatedMovies() } returns unsortedRatedMovies

        val result = useCase.getRatedMovies()

        assertThat(result).hasSize(2)
        assertThat(result[0].userRate).isGreaterThan(result[1].userRate)
        assertThat(result[0].movie.name).isEqualTo("Movie Two")
    }
}
