package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto

fun CategoryRemoteDto.toLocalMovieCategoryDto(): LocalMovieCategoryDto =
    LocalMovieCategoryDto(
        categoryId = id.toLong()
    )

fun List<CategoryRemoteDto>.toLocalDtoList(): List<LocalMovieCategoryDto> =
    map { it.toLocalMovieCategoryDto() }