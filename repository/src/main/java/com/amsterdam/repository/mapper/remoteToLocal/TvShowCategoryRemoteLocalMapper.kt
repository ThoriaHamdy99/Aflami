package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto

fun CategoryRemoteDto.toLocalTvShowCategoryDto(storedLanguage: String): LocalTvShowCategoryDto =
    LocalTvShowCategoryDto(
        categoryId = this.id.toLong()
    )

fun List<CategoryRemoteDto>.toLocalTvShowCategoryDtoList(storedLanguage: String): List<LocalTvShowCategoryDto> =
    map { it.toLocalTvShowCategoryDto(storedLanguage) }