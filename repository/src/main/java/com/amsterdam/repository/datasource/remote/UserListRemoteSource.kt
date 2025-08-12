package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse

interface UserListRemoteSource {
    suspend fun createNewList(listName: String, language: String, ): CreateUserListResponse
    suspend fun getUserLists(accountId: Int, page: Int): RemoteUserListResponse
    suspend fun deleteList(listId: Long)

    suspend fun addMovieToList(listId: Long, movieId: Long): AddItemToListResponse
    suspend fun getMoviesFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteMovieFromList(listId: Long, movieId: Long)
    suspend fun getUserLists(
        accountId: Int,
        page: Int,
        sessionId: String
    ): RemoteUserListResponse
    suspend fun addMovieToList(
        listId: Long,
        sessionId: String,
        movieId: Int,
    ): AddItemToListResponse

    suspend fun getMoviesAndTvShowsFromList(listId: Long, page: Int): UserListDetailsResponse
    suspend fun deleteList(listId: Long, sessionId: String)
    suspend fun removeMovieFromList(listId: Long, sessionId: String, movieId: Long)
}
