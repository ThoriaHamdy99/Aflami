package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.WishListRepository
import com.amsterdam.domain.useCase.list.GetListMediaItemsFromListUseCase
import com.amsterdam.entity.WishList
import com.amsterdam.repository.datasource.remote.WishListRemoteDataSource
import com.amsterdam.repository.mapper.toMovieEntity
import com.amsterdam.repository.mapper.toTvShowEntity
import com.amsterdam.repository.mapper.toEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class WishListRepositoryImpl @Inject constructor(
    private val userListDataSource: WishListRemoteDataSource,
    private val preferences: AppPreferencesRepository,
) : WishListRepository {

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

    override suspend fun getMoviesAndTvShowsFromList(
        listId: Long,
        page: Int
    ): GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems {
        val items = userListDataSource.getMoviesAndTvShowsFromList(listId, page).items

        val tvShows = items.filter { it.mediaType == "tv" }.map { it.toTvShowEntity() }
        val movies = items.filter { it.mediaType == "movie" }.map { it.toMovieEntity() }

        return GetListMediaItemsFromListUseCase.ListScreenDetailsMediaItems(
            listDetailsMovies = movies,
            listDetailsShows = tvShows
        )
    }

    override suspend fun deleteList(listId: Long) = userListDataSource.deleteList(listId)

    override suspend fun getWishLists(accountId: Int, page: Int): List<WishList> {
        return userListDataSource.getWishLists(accountId, page).results
            .map { it.toEntity() }
    }

    override suspend fun removeMovieFromList(listId: Long, movieId: Long) {
        userListDataSource.deleteMovieFromList(listId, movieId)
    }
}
