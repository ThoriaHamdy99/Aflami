package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserListApiService {
    @FormUrlEncoded
    @POST("list")
    suspend fun createNewList(
        @Field("name") listName: String,
        @Field("description") description: String = "",
        @Field("language") language: String,
    ): CreateUserListRemoteResponse

    @GET("account/{account_id}/lists")
    suspend fun getUserLists(
        @Path("account_id") accountId: Int = 0,
        @Query("page") page: Int = 1,
    ): UserListRemoteResponse

    @GET("list/{list_id}")
    suspend fun getMoviesAndTvShowsFromList(
        @Path("list_id") listId: Long,
        @Query("page") page: Int,
    ): UserListDetailsRemoteResponse

    @DELETE("list/{list_id}")
    suspend fun deleteList(
        @Path("list_id") listId: Long,
    )

    @FormUrlEncoded
    @POST("list/{list_id}/add_item")
    suspend fun addMediaItemToList(
        @Path("list_id") listId: Long,
        @Field("media_id") movieId: Long,
    ): AddItemToListRemoteResponse

    @FormUrlEncoded
    @POST("list/{list_id}/remove_item")
    suspend fun removeMovieFromList(
        @Path("list_id") listId: Long,
        @Field("media_id") movieId: Long,
    )

}
