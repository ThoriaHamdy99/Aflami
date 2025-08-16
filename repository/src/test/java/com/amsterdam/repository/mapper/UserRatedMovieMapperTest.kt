package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserRatedMovieMapperTest {
    private val ratedMovieDto = MovieItemRemoteDto(
        id = 101L, title = "Rated Movie", overview = "An overview.",
        posterPath = "/poster.jpg", backdropPath = null, releaseDate = "2023-10-26",
        voteAverage = 8.8, genreIds = listOf(28), popularity = 1234.5,
        originCountry = listOf("GB"), runtime = 120, adult = false,
        originalLanguage = "en", originalTitle = "Rated Movie", video = false,
        voteCount = 1000, rating = 8.5f
    )

    private val ratedMovieDtoWithZeroRating = ratedMovieDto.copy(
        id = 102L,
        rating = 0f
    )

    @Nested
    inner class ToMovieUserRateEntityTest {
        @Test
        fun `toMovieUserRateEntity should map Dto to UserRatedMovie entity`() {
            val expectedMovie = ratedMovieDto.toEntity()

            val result = ratedMovieDto.toMovieUserRateEntity(ratedMovieDto)

            assertThat(result).isEqualTo(
                UserRatedMovie(
                    movie = expectedMovie,
                    userRate = 8
                )
            )
        }

        @Test
        fun `toMovieUserRateEntity should map zero rating correctly`() {
            val expectedMovie = ratedMovieDtoWithZeroRating.toEntity()

            val result = ratedMovieDtoWithZeroRating.toMovieUserRateEntity(ratedMovieDtoWithZeroRating)

            assertThat(result).isEqualTo(
                UserRatedMovie(
                    movie = expectedMovie,
                    userRate = 0
                )
            )
        }
    }

    @Nested
    inner class ToMovieUserRateEntityListTest {
        @Test
        fun `toMovieUserRateEntityList should map a list of Dtos correctly`() {
            val dtoList = listOf(ratedMovieDto, ratedMovieDtoWithZeroRating)

            val result = dtoList.toMovieUserRateEntityList()

            assertThat(result).hasSize(2)
            assertThat(result[0].userRate).isEqualTo(8)
            assertThat(result[0].movie.id).isEqualTo(101L)
            assertThat(result[1].userRate).isEqualTo(0)
            assertThat(result[1].movie.id).isEqualTo(102L)
        }

        @Test
        fun `toMovieUserRateEntityList should return an empty list when given an empty list`() {
            val emptyDtoList = emptyList<MovieItemRemoteDto>()

            val result = emptyDtoList.toMovieUserRateEntityList()

            assertThat(result).isEmpty()
        }
    }
}