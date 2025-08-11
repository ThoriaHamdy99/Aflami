package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.UserList
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
import com.amsterdam.repository.mapper.remote.toMovie
import com.amsterdam.repository.mapper.remote.toUserList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListDataSource: UserListRemoteSource,
    private val preferences: AppPreferencesRepository,
) : UserListRepository {

    override suspend fun addMovieToList(listId: Long, movieId: Long) {
        val response = userListDataSource.addMovieToList(listId, movieId)
        if (!response.success) throw UnknownException()
    }

    override suspend fun createNewList(listName: String): Int {
        return userListDataSource.createNewList(
            listName = listName,
            language = preferences.getAppLanguage().first(),
        ).listId
    }

    override suspend fun getMoviesFromList(listId: Long, page: Int): List<Movie> {
        return userListDataSource.getMoviesFromList(listId, page).items
            .map { it.toMovie() }
    }

    override suspend fun deleteList(listId: Long) = userListDataSource.deleteList(listId)

    override suspend fun getUserLists(accountId: Int, page: Int): List<UserList> {
        return userListDataSource.getUserLists(accountId, page).results
            .map { it.toUserList() }
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        userListDataSource.deleteMovieFromList(listId, movieId)
    }
}
