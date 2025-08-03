package com.amsterdam.domain.repository

import com.amsterdam.entity.UserListItem

interface UserListRepository {
    suspend fun getUserListDetails(listId: Long, page: Int): List<UserListItem>
}