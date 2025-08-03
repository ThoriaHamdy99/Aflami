package com.amsterdam.domain.repository

import com.amsterdam.entity.UserList

interface UserListRepository {
    suspend fun getUserLists(
        accountId: Int = 0,
        page: Int = 1,
        sessionId: String
    ): List<UserList>
} 