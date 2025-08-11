package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetListMediaItemsFromListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): ListScreenDetailsMediaItems {
        val items = userListRepository.getMoviesAndShowsFromList(listId, page)
        return ListScreenDetailsMediaItems(
            listDetailsShows = items.first,
            listDetailsMovies = items.second
        )
    }

    data class ListScreenDetailsMediaItems(
        val listDetailsMovies: List<Movie>,
        val listDetailsShows: List<TvShow>
    )
}
