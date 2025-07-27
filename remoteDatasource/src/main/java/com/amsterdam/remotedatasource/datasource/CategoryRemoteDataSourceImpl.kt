package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.serviceProvider.CategoryServiceProvider
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import javax.inject.Inject

class CategoryRemoteDataSourceImpl @Inject constructor(
    private val categoryServiceProvider: CategoryServiceProvider
) : CategoryRemoteSource {

    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return categoryServiceProvider.getMovieCategories()
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return categoryServiceProvider.getTvShowCategories()
    }
}