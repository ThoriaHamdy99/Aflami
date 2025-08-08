package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import retrofit2.http.GET

interface CategoryApiService {
    @GET("genre/movie/list")
    suspend fun getMovieCategories(): RemoteCategoryResponse

    @GET("genre/tv/list")
    suspend fun getTvShowCategories(): RemoteCategoryResponse

}