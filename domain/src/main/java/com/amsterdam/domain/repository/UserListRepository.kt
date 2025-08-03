package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface UserListRepository {
    suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie>
}