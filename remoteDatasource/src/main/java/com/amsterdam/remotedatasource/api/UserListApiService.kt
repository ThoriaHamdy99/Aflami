package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserListApiService {

    @GET(USER_LISTS_ENDPOINT)
    suspend fun getUserLists(
        @Path("account_id") accountId: Int=0,
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String
    ): RemoteUserListResponse

    companion object {
        const val USER_LISTS_ENDPOINT = "account/{account_id}/lists"
    }
} 