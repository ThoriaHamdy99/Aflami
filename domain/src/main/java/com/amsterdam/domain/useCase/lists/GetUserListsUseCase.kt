package com.amsterdam.domain.useCase.lists

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList

class GetUserListsUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        page: Int = 1,
        sessionId: String
    ): List<UserList> {
        return userListRepository.getCustomLists(accountId, page, sessionId)
    }
} 