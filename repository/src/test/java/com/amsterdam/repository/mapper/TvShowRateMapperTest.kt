package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TvShowRateMapperTest {
    private val ratedTvShowDto = TvShowItemRemoteDto(
        id = 201L,
        title = "Rated TV Show",
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = null,
        releaseDate = "2023-10-26",
        voteAverage = 8.8,
        genreIds = emptyList(),
        popularity = 1234.5,
        seasonCount = 3,
        originCountry = listOf("US"),
        adult = false,
        originalLanguage = "en",
        originalTitle = "Rated TV Show",
        voteCount = 1000,
        rating = 9.5f
    )

    private val ratedTvShowDtoWithZeroRating = ratedTvShowDto.copy(
        id = 202L,
        rating = 0f
    )

    @Nested
    inner class ToTvShowUserRateEntityTest {
        @Test
        fun `toTvShowUserRateEntity should map Dto to UserRatedTvShow entity`() {
            val expectedTvShow = ratedTvShowDto.toEntity()

            val result = ratedTvShowDto.toTvShowUserRateEntity(ratedTvShowDto)

            assertThat(result).isEqualTo(
                UserRatedTvShow(
                    tvShow = expectedTvShow,
                    userRate = 9
                )
            )
        }

        @Test
        fun `toTvShowUserRateEntity should map zero rating correctly`() {
            val expectedTvShow = ratedTvShowDtoWithZeroRating.toEntity()

            val result = ratedTvShowDtoWithZeroRating.toTvShowUserRateEntity(ratedTvShowDtoWithZeroRating)

            assertThat(result).isEqualTo(
                UserRatedTvShow(
                    tvShow = expectedTvShow,
                    userRate = 0
                )
            )
        }
    }

    @Nested
    inner class ToTvShowUserRateEntityListTest {
        @Test
        fun `toTvShowUserRateEntityList should map a list of Dtos correctly`() {
            val dtoList = listOf(ratedTvShowDto, ratedTvShowDtoWithZeroRating)

            val result = dtoList.toTvShowUserRateEntityList()

            assertThat(result).hasSize(2)
            assertThat(result[0].userRate).isEqualTo(9)
            assertThat(result[0].tvShow.id).isEqualTo(201L)
            assertThat(result[1].userRate).isEqualTo(0)
            assertThat(result[1].tvShow.id).isEqualTo(202L)
        }

        @Test
        fun `toTvShowUserRateEntityList should return an empty list when given an empty list`() {
            val emptyDtoList = emptyList<TvShowItemRemoteDto>()

            val result = emptyDtoList.toTvShowUserRateEntityList()

            assertThat(result).isEmpty()
        }
    }
}