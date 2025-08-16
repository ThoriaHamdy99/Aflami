package com.amsterdam.repository.mapper

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.remote.UserListItemRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserListItemMapperTest {
    private val movieDto = UserListItemRemoteDto(
        id = 101L,
        title = "Test Movie",
        name = null,
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2023-10-26",
        firstAirDate = null,
        voteAverage = 8.8,
        genreIds = listOf(28),
        popularity = 1234.5,
        originalLanguage = "en",
        mediaType = "movie",
        adult = false
    )

    private val tvShowDto = UserListItemRemoteDto(
        id = 201L,
        title = null,
        name = "Test TV Show",
        overview = "A TV overview.",
        posterPath = "/tv_poster.jpg",
        backdropPath = "/tv_backdrop.jpg",
        releaseDate = null,
        firstAirDate = "2022-12-25",
        voteAverage = 9.1,
        genreIds = listOf(10759),
        popularity = 567.8,
        originalLanguage = "fr",
        mediaType = "tv",
        adult = false
    )

    private val dtoWithNulls = UserListItemRemoteDto(
        id = 301L,
        title = null,
        name = null,
        overview = "Another overview.",
        posterPath = null,
        backdropPath = null,
        releaseDate = null,
        firstAirDate = null,
        voteAverage = null,
        genreIds = null,
        popularity = null,
        originalLanguage = "de",
        mediaType = "person",
        adult = true
    )

    @Nested
    inner class ToMovieEntityTest {
        @Test
        fun `toMovieEntity should map movie Dto with poster image`() {
            val result = movieDto.toMovieEntity()

            assertThat(result.id).isEqualTo(101L)
            assertThat(result.name).isEqualTo("Test Movie")
            assertThat(result.releaseDate).isEqualTo(LocalDate.parse("2023-10-26"))
            assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
            assertThat(result.categories).containsExactly(MovieGenre.ACTION)
        }

        @Test
        fun `toMovieEntity should map with backdrop image when isPoster is false`() {
            val result = movieDto.toMovieEntity(isPoster = false)

            assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/backdrop.jpg")
        }

        @Test
        fun `toMovieEntity should handle null values and map to default values`() {
            val result = dtoWithNulls.toMovieEntity()

            assertThat(result.name).isEmpty()
            assertThat(result.posterUrl).isEmpty()
            assertThat(result.releaseDate).isEqualTo(LocalDate.fromEpochDays(0))
            assertThat(result.rating).isEqualTo(0f)
            assertThat(result.categories).isEmpty()
        }
    }

    @Nested
    inner class ToTvShowEntityTest {
        @Test
        fun `toTvShowEntity should map with backdrop image when isPoster is false`() {
            val result = tvShowDto.toTvShowEntity(isPoster = false)

            assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/tv_backdrop.jpg")
        }

        @Test
        fun `toTvShowEntity should handle null values and map to default values`() {
            val result = dtoWithNulls.toTvShowEntity()

            assertThat(result.name).isEmpty()
            assertThat(result.posterUrl).isEmpty()
            assertThat(result.airDate).isEqualTo(LocalDate.fromEpochDays(0))
            assertThat(result.rating).isEqualTo(0f)
            assertThat(result.categories).isEmpty()
        }
    }
}