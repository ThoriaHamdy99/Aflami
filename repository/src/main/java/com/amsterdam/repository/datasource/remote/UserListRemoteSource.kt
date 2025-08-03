package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.RemoteUserListResponse

interface UserListRemoteSource {
    suspend fun getCustomLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse
} 