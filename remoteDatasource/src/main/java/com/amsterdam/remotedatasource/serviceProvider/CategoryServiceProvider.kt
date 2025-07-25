package com.amsterdam.remotedatasource.serviceProvider

import com.amsterdam.repository.dto.remote.RemoteCategoryResponse

interface CategoryServiceProvider {
    suspend fun getMovieCategories(): RemoteCategoryResponse
    suspend fun getTvShowCategories(): RemoteCategoryResponse
}