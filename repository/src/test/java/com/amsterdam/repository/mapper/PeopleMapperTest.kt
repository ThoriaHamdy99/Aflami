package com.amsterdam.repository.mapper

import com.amsterdam.entity.People
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PeopleMapperTest {
    @Nested
    inner class ToEntityTest {
        @Test
        fun `toEntity should map RemotePeopleItemDto to People entity`() {
            val expectedEntity = People(
                id = 1L,
                name = "Tom Holland",
                imageUrl = "https://image.tmdb.org/t/p/w300/tom_holland.jpg" // Uses w300
            )

            val result = remotePersonDto.toEntity()

            assertThat(result).isEqualTo(expectedEntity)
        }

        @Test
        fun `toEntity should map imageUrl to empty string when profilePath is null`() {
            val expectedEntity = People(
                id = 2L,
                name = "Zendaya",
                imageUrl = ""
            )

            val result = remotePersonDtoWithNullImage.toEntity()

            assertThat(result).isEqualTo(expectedEntity)
        }
    }

    @Nested
    inner class ToEntityListTest {
        @Test
        fun `toEntityList should map a list of Dtos to a list of People entities`() {
            val dtoList = listOf(remotePersonDto, remotePersonDtoWithNullImage)

            val result = dtoList.toEntityList()

            assertThat(result).isEqualTo(
                listOf(
                    remotePersonDto.toEntity(),
                    remotePersonDtoWithNullImage.toEntity()
                )
            )
        }

        @Test
        fun `toEntityList should return an empty list when given an empty list`() {
            val emptyDtoList = emptyList<RemotePeopleItemDto>()

            val result = emptyDtoList.toEntityList()

            assertThat(result).isEmpty()
        }
    }

    private val remotePersonDto = RemotePeopleItemDto(
        id = 1,
        name = "Tom Holland",
        profilePath = "/tom_holland.jpg",
        adult = false,
        gender = 2,
        popularity = 150.0,
        mediaType = "person",
        originalName = "Tom Holland",
        knownForDepartment = "Acting"
    )

    private val remotePersonDtoWithNullImage = remotePersonDto.copy(
        id = 2,
        name = "Zendaya",
        profilePath = null
    )
}