package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MovieCategoryMapperTest {
    @Nested
    inner class ToLocalMovieCategoryDtoTest {
        @Test
        fun `toLocalMovieCategoryDto should map CategoryRemoteDto to MovieCategoryLocalDto`() {
            val result = remoteCategory1.toLocalMovieCategoryDto()

            assertThat(result).isEqualTo(
                MovieCategoryLocalDto(categoryId = 28L)
            )
        }
    }

    @Nested
    inner class ToLocalTvShowDtoListTest {
        @Test
        fun `toLocalTvShowDtoList should map a list of CategoryRemoteDto to a list of MovieCategoryLocalDto`() {
            val remoteList = listOf(remoteCategory1, remoteCategory2)

            val result = remoteList.toLocalTvShowDtoList()

            assertThat(result).isEqualTo(
                listOf(
                    MovieCategoryLocalDto(categoryId = 28L),
                    MovieCategoryLocalDto(categoryId = 12L)
                )
            )
        }

        @Test
        fun `toLocalTvShowDtoList should return an empty list when given an empty list`() {
            val emptyRemoteList = emptyList<CategoryRemoteDto>()

            val result = emptyRemoteList.toLocalTvShowDtoList()

            assertThat(result).isEmpty()
        }
    }

    private val remoteCategory1 = CategoryRemoteDto(id = 28, name = "Action")
    private val remoteCategory2 = CategoryRemoteDto(id = 12, name = "Adventure")
}