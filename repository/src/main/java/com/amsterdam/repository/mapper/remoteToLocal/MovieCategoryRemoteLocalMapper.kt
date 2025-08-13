package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun RemoteCategoryDto.toLocalMovieCategoryDto(): MovieCategoryLocalDto =
    MovieCategoryLocalDto(
        categoryId = id.toLong()
    )

fun List<RemoteCategoryDto>.toLocalDtoList(): List<MovieCategoryLocalDto> =
    map { it.toLocalMovieCategoryDto() }