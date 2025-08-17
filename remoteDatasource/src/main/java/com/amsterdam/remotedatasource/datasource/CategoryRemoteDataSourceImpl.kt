package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.logger.Loggable
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.dto.remote.CategoryRemoteResponse
import javax.inject.Inject

class CategoryRemoteDataSourceImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryRemoteDataSource, Loggable {

    override suspend fun getMovieCategories(): CategoryRemoteResponse {
        return responseCall(logger = logger, execute = { categoryApiService.getMovieCategories() })
    }

    override suspend fun getTvShowCategories(): CategoryRemoteResponse {
        return responseCall(logger = logger, execute = { categoryApiService.getTvShowCategories() })
    }
}