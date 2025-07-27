package com.amsterdam.remotedatasource.serviceProvider.implementation

import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.serviceProvider.CategoryServiceProvider
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import javax.inject.Inject

class CategoryServiceProviderImpl @Inject constructor(
    private val categoryApiService: CategoryApiService
) : CategoryServiceProvider {
    override suspend fun getMovieCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getMovieCategories() }
    }

    override suspend fun getTvShowCategories(): RemoteCategoryResponse {
        return responseCall { categoryApiService.getTvShowCategories() }
    }
}