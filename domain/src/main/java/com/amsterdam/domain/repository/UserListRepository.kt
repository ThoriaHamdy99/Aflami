package com.amsterdam.domain.repository

import com.amsterdam.entity.UserList

interface UserListRepository {
    suspend fun getCustomLists(
        accountId: Int,
        page: Int = 1,
        sessionId: String
    ): List<UserList>
} 