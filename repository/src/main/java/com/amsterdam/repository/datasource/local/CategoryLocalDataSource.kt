package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

interface CategoryLocalDataSource {
    suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>)
    suspend fun getMovieCategories(): List<LocalMovieCategoryDto>
    suspend fun upsertTvShowCategories(categories: List<LocalTvShowCategoryDto>)
    suspend fun getTvShowCategories(): List<LocalTvShowCategoryDto>
}
