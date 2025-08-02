package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto

interface CategoryLocalSource {
    suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun getMovieCategories(storedLanguage: String): List<LocalMovieCategoryDto>
}
