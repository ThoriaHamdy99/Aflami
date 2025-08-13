package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import javax.inject.Inject

class CategoryRemoteDataSourceImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryRemoteSource {

    override suspend fun getMovieCategories(): CategoryRemoteResponse {
        return responseCall { categoryApiService.getMovieCategories() }
    }

    override suspend fun getTvShowCategories(): CategoryRemoteResponse {
        return responseCall { categoryApiService.getTvShowCategories() }
    }
}