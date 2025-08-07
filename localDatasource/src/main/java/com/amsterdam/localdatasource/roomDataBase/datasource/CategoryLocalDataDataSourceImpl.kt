package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import javax.inject.Inject

class CategoryLocalDataDataSourceImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryLocalDataSource {
    override suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>) {
        dao.upsertAllMovieCategories(categories)
    }

    override suspend fun upsertTvShowCategories(categories: List<LocalTvShowCategoryDto>) {
        dao.upsertAllTvShowCategories(categories)
    }

    override suspend fun getMovieCategories(): List<LocalMovieCategoryDto> {
        return dao.getAllMovieCategories()
    }

    override suspend fun getTvShowCategories(): List<LocalTvShowCategoryDto> {
        return dao.getAllTvShowCategories()
    }

}


