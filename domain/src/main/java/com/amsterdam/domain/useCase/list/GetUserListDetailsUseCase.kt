package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserListItem

class GetUserListDetailsUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): List<UserListItem> {
        return userListRepository.getUserListDetails(listId, page)
    }
}
