package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteCategoryResponse

interface CategoryRemoteSource {
    suspend fun getMovieCategories(): RemoteCategoryResponse
    suspend fun getTvShowCategories(): RemoteCategoryResponse
}