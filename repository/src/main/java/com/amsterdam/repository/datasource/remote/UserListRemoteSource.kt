package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteUserListResponse

interface UserListRemoteSource {
    suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse
} 