package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserListItem
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.UserListItemRemoteMapper
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListDataSource: UserListRemoteSource,
    private val listItemRemoteMapper: UserListItemRemoteMapper
) : UserListRepository {
    override suspend fun getUserListDetails(listId: Long, page: Int): List<UserListItem> {
        return listItemRemoteMapper.toEntityList(
            userListDataSource.getUserListDetails(listId, page).items
        )
    }
}
