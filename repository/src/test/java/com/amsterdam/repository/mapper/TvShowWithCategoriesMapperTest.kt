package com.amsterdam.repository.mapper

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TvShowWithCategoriesMapperTest {
    @Nested
    inner class ToEntityTest {
        @Test
        fun `toEntity should map TvShowWithCategories to TvShow entity with distinct categories`() {
            val tvShowWithCategories = TvShowWithCategories(
                tvShow = baseTvShowLocalDto,
                categories = listOf(
                    TvShowCategoryLocalDto(categoryId = 10759L),
                    TvShowCategoryLocalDto(categoryId = 18L)      // Drama
                )
            )

            val result = tvShowWithCategories.toEntity()

            assertThat(result).isEqualTo(
                TvShow(
                    id = 201L,
                    name = "Test TV Show",
                    description = "An overview.",
                    posterUrl = "/poster.jpg",
                    airDate = LocalDate.parse("2023-10-26"),
                    rating = 8.5f,
                    categories = listOf(TvShowGenre.ACTION_ADVENTURE, TvShowGenre.DRAMA),
                    popularity = 1500.0,
                    seasonCount = 5,
                    originCountry = "US"
                )
            )
        }

        @Test
        fun `toEntity should handle duplicate categories by using distinctBy`() {
            val tvShowWithDuplicateCategories = TvShowWithCategories(
                tvShow = baseTvShowLocalDto,
                categories = listOf(
                    TvShowCategoryLocalDto(categoryId = 10759L),
                    TvShowCategoryLocalDto(categoryId = 18L),
                    TvShowCategoryLocalDto(categoryId = 10759L)
                )
            )

            val result = tvShowWithDuplicateCategories.toEntity()

            assertThat(result.categories).containsExactly(
                TvShowGenre.ACTION_ADVENTURE,
                TvShowGenre.DRAMA
            ).inOrder()
        }

        @Test
        fun `toEntity should handle an empty list of categories`() {
            val tvShowWithNoCategories = TvShowWithCategories(
                tvShow = baseTvShowLocalDto,
                categories = emptyList()
            )

            val result = tvShowWithNoCategories.toEntity()

            assertThat(result.id).isEqualTo(201L)
            assertThat(result.name).isEqualTo("Test TV Show")
            assertThat(result.categories).isEmpty()
        }
    }

    private val baseTvShowLocalDto = TvShowLocalDto(
        tvShowId = 201L,
        storedLanguage = "en",
        name = "Test TV Show",
        description = "An overview.",
        poster = "/poster.jpg",
        airDate = LocalDate.parse("2023-10-26"),
        rating = 8.5f,
        popularity = 1500.0,
        seasonCount = 5,
        originCountry = "US"
    )
}