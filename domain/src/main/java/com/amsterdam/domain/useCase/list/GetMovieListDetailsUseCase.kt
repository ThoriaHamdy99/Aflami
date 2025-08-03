package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie

class GetMovieListDetailsUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): List<Movie> {
        return userListRepository.getMovieListDetails(listId, page)
    }
}
