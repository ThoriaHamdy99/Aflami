package com.example.localdatasource.utils

import com.example.repository.dto.local.LocalMovieCategoryDto

fun createLocalMovieCategoryDto(
    categoryId: Long = 0L,
    storedLanguage: String = "en",
    name: String = "Default Name"
): LocalMovieCategoryDto {
    return LocalMovieCategoryDto(
        categoryId = categoryId,
        storedLanguage = storedLanguage,
        name = name
    )
}
