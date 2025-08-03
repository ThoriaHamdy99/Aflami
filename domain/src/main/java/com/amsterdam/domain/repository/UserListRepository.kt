package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface UserListRepository {
    suspend fun getMovieListDetails(listId: Long, page: Int): List<Movie>
}