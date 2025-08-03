package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import javax.inject.Inject

class UserListRemoteDataSourceImpl @Inject constructor(
    private val customListApiService: UserListApiService
) : UserListRemoteSource {
    
    override suspend fun getCustomLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse {
        return customListApiService.getUserLists(accountId, page, sessionId)
    }
} 