package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.toUserList
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListRemoteSource: UserListRemoteSource
) : UserListRepository {
    
    override suspend fun getCustomLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): List<UserList> {
        return userListRemoteSource.getCustomLists(accountId, page, sessionId)
            .results.map { it.toUserList() }
    }
} 