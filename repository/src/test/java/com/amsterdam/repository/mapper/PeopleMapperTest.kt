package com.amsterdam.repository.mapper

import com.amsterdam.entity.People
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PeopleMapperTest {
    @Test
    fun `toEntity should map RemotePeopleItemDto to People entity`() {
        val result = remotePersonDto.toEntity()

        assertThat(result).isEqualTo(expectedPersonEntity)
    }

    @Test
    fun `toEntity should map imageUrl to empty string when profilePath is null`() {
        val result = remotePersonDtoWithNullImage.toEntity()

        assertThat(result).isEqualTo(expectedPersonWithNullImage)
    }

    @Test
    fun `toEntityList should map a list of Dtos to a list of People entities`() {
        val dtoList = listOf(remotePersonDto, remotePersonDtoWithNullImage)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedPeopleList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<RemotePeopleItemDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
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

    private val expectedPersonEntity = People(
        id = 1L,
        name = "Tom Holland",
        imageUrl = "https://image.tmdb.org/t/p/w300/tom_holland.jpg"
    )

    private val expectedPersonWithNullImage = People(
        id = 2L,
        name = "Zendaya",
        imageUrl = ""
    )

    private val expectedPeopleList = listOf(
        expectedPersonEntity,
        expectedPersonWithNullImage
    )
}