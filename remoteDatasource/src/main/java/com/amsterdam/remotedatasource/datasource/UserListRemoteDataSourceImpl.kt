package com.amsterdam.remotedatasource.datasource

import android.util.Log
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import javax.inject.Inject

class UserListRemoteDataSourceImpl @Inject constructor(
    private val userListApiService: UserListApiService
) : UserListRemoteSource {
    
    override suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse {
        return responseCall {  userListApiService.getUserLists(accountId, page, sessionId) }
    }
} 