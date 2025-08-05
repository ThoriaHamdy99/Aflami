package com.amsterdam.domain.repository

import com.amsterdam.entity.UserList
import com.amsterdam.entity.Movie

interface UserListRepository {
    suspend fun getUserLists(
        accountId: Int = 0,
        page: Int = 1,
        sessionId: String
    ): List<UserList>

    suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie>
    suspend fun deleteList(listId: Long)
    suspend fun removeMovieFromList(listId: Long, movieId: Long)
}