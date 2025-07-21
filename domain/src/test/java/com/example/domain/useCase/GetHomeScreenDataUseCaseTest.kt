package com.example.domain.useCase

import com.example.domain.useCase.utils.specificMovieList
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class GetHomeScreenDataUseCaseTest {
    private lateinit var getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var getHomeScreenDataUseCase: GetHomeScreenDataUseCase

    @BeforeEach
    fun setUp() {
        getTopRatedMoviesUseCase = mockk(relaxed = true)
        getPopularMoviesUseCase = mockk(relaxed = true)
        getHomeScreenDataUseCase = GetHomeScreenDataUseCase(
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase
        )
    }

    @Test
    fun `should return home screen data when both use cases return data`() = runTest {
        // Given
        coEvery {  getTopRatedMoviesUseCase () } returns specificMovieList
        coEvery {  getPopularMoviesUseCase () } returns specificMovieList

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(GetHomeScreenDataUseCase.HomeScreenData(specificMovieList , specificMovieList))
    }

    @Test
    fun `should throw exception when getTopRatedMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Top rated error"
        coEvery { getTopRatedMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should throw exception when getPopularMoviesUseCase throws`() = runTest {
        // Given
        val expectedMessage = "Popular error"
        coEvery { getTopRatedMoviesUseCase() } returns specificMovieList
        coEvery { getPopularMoviesUseCase() } throws Exception(expectedMessage)

        // When
        val exception = assertThrows<Exception> {
            getHomeScreenDataUseCase()
        }

        // Then
        assertThat(exception).hasMessageThat().isEqualTo(expectedMessage)
    }

    @Test
    fun `should return empty lists when both use cases return empty lists`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns emptyList()
        coEvery { getPopularMoviesUseCase() } returns emptyList()

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(
            GetHomeScreenDataUseCase.HomeScreenData(
                topRatedMovies = emptyList(),
                popularMovies = emptyList()
            )
        )
    }

    @Test
    fun `should return home screen data with topRated empty and popular non-empty`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns emptyList()
        coEvery { getPopularMoviesUseCase() } returns specificMovieList

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(
            GetHomeScreenDataUseCase.HomeScreenData(
                topRatedMovies = emptyList(),
                popularMovies = specificMovieList
            )
        )
    }

    @Test
    fun `should return home screen data with popular empty and topRated non-empty`() = runTest {
        // Given
        coEvery { getTopRatedMoviesUseCase() } returns specificMovieList
        coEvery { getPopularMoviesUseCase() } returns emptyList()

        // When
        val result = getHomeScreenDataUseCase()

        // Then
        assertThat(result).isEqualTo(
            GetHomeScreenDataUseCase.HomeScreenData(
                topRatedMovies = specificMovieList,
                popularMovies = emptyList()
            )
        )
    }

}