package com.example.localdatasource.utils

import com.example.repository.dto.local.LocalTvShowCategoryDto

fun createLocalTvShowCategoryDto(
    categoryId: Long = 0,
    storedLanguage: String = "en",
    name: String = "Default"
): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(categoryId, storedLanguage, name)
}