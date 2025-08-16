package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class CheckIsMovieInListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(
        movieId: Long,
        listId: Long
    ): Boolean {
        return userListRepository.checkIsMovieInList(movieId = movieId, listId = listId)
    }
}