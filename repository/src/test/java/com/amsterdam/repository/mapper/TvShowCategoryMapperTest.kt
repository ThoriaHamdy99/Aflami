package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TvShowCategoryMapperTest {
    private val remoteDto1 = CategoryRemoteDto(id = 10759, name = "Action & Adventure")
    private val remoteDto2 = CategoryRemoteDto(id = 16, name = "Animation")

    @Nested
    inner class ToLocalTvShowCategoryDtoTest {
        @Test
        fun `toLocalTvShowCategoryDto should map CategoryRemoteDto to TvShowCategoryLocalDto`() {
            val storedLanguage = "en"

            val result = remoteDto1.toLocalTvShowCategoryDto(storedLanguage)

            assertThat(result).isEqualTo(
                TvShowCategoryLocalDto(categoryId = 10759L)
            )
        }
    }

    @Nested
    inner class ToLocalTvShowDtoListTest {
        @Test
        fun `toLocalTvShowDtoList should map a list of Dtos to a list of TvShowCategoryLocalDto`() {
            val dtoList = listOf(remoteDto1, remoteDto2)
            val storedLanguage = "ar"

            val result = dtoList.toLocalTvShowDtoList(storedLanguage)

            assertThat(result).isEqualTo(
                listOf(
                    TvShowCategoryLocalDto(categoryId = 10759L),
                    TvShowCategoryLocalDto(categoryId = 16L)
                )
            )
        }

        @Test
        fun `toLocalTvShowDtoList should return an empty list when given an empty list`() {
            val emptyDtoList = emptyList<CategoryRemoteDto>()

            val result = emptyDtoList.toLocalTvShowDtoList("en")

            assertThat(result).isEmpty()
        }
    }
}