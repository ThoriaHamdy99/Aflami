package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.UserListDetailsResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserListApiService {

    @GET(GET_USER_LIST_DETAILS)
    suspend fun getMoviesFromList(
        @Path(LIST_ID) listId: Long,
        @Query(PAGE) page: Int
    ): UserListDetailsResponse

    @DELETE(DELETE_LIST)
    suspend fun deleteList(
        @Path(LIST_ID) listId: Long,
        @Query(SESSION_ID) sessionId: String
    )

    companion object {
        private const val PAGE = "page"
        private const val LIST_ID = "list_id"
        private const val SESSION_ID = "session_id"

        private const val GET_USER_LIST_DETAILS = "list/{list_id}"
        private const val DELETE_LIST = "list/{list_id}"
    }
}