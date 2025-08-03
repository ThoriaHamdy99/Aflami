package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.UserListDetailsResponse

interface UserListRemoteSource {
    suspend fun getMoviesFromList(listId: Long, page: Int): UserListDetailsResponse
}