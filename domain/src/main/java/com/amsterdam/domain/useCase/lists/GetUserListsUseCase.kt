package com.amsterdam.domain.useCase.lists

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList

class GetUserListsUseCase(
    private val userListRepository: UserListRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(
        page: Int = 1,
    ): List<UserList> {
        val sessionId = authenticationRepository.getSessionId()
        return userListRepository.getUserLists(page = page, sessionId = sessionId)

    }

} 