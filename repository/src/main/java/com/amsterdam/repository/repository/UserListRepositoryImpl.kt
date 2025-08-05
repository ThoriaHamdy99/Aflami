package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.UserList
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.toMovie
import com.amsterdam.repository.mapper.remote.toUserList
import javax.inject.Inject

class UserListRepositoryImpl
    @Inject
    constructor(
        private val userListDataSource: UserListRemoteSource,
        private val authenticationRepository: AuthenticationRepository,
    ) : UserListRepository {
        override suspend fun getMoviesFromList(
            listId: Long,
            page: Int,
        ): List<Movie> =
            userListDataSource
                .getMoviesFromList(listId, page)
                .items
                .map { it.toMovie() }

        override suspend fun deleteList(listId: Long) {
            val sessionId = authenticationRepository.getSessionId()
            userListDataSource.deleteList(listId, sessionId)
        }

        override suspend fun getUserLists(
            accountId: Int,
            page: Int,
            sessionId: String,
        ): List<UserList> =
            userListRemoteSource
                .getUserLists(accountId, page, sessionId)
                .results
                .map { it.toUserList() }

        override suspend fun removeMovieFromList(
            listId: Long,
            movieId: Long,
        ) {
            val sessionId = authenticationRepository.getSessionId()
            userListDataSource.removeMovieFromList(listId, sessionId, movieId)
        }
    }
