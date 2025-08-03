package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.UserListItemRemoteMapper
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListDataSource: UserListRemoteSource,
    private val listItemRemoteMapper: UserListItemRemoteMapper
) : UserListRepository {
    override suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie> {
        return listItemRemoteMapper.toEntityList(
            userListDataSource.getMoviesFromList(listId, page).items
        )
    }
}
