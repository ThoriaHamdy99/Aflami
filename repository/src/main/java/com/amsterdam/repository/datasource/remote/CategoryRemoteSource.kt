package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.CategoryRemoteResponse

interface CategoryRemoteSource {
    suspend fun getMovieCategories(): CategoryRemoteResponse
    suspend fun getTvShowCategories(): CategoryRemoteResponse
}