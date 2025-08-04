package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.UserListItemRemoteMapper
import com.amsterdam.repository.security.CryptoData
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListDataSource: UserListRemoteSource,
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val listItemRemoteMapper: UserListItemRemoteMapper,
    val cryptoData: CryptoData,
) : UserListRepository {
    override suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie> {
        return listItemRemoteMapper.toEntityList(
            userListDataSource.getMoviesFromList(listId, page).items
        )
    }

    override suspend fun deleteList(listId: Long) {
        val sessionId = cryptoData.decryptString(authenticationLocalSource.getCachedSessionId())
            ?: throw UnknownException()
        userListDataSource.deleteList(listId, sessionId)
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        val sessionId = cryptoData.decryptString(authenticationLocalSource.getCachedSessionId())
            ?: throw UnknownException()
        userListDataSource.removeMovieFromList(listId, sessionId, movieId)
    }
}
