package com.amsterdam.repository.mapper

import com.amsterdam.entity.UserList
import com.amsterdam.repository.dto.remote.UserListRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserListMapperTest {
    @Test
    fun `toEntity should map UserListRemoteDto to UserList entity`() {
        val result = userListRemoteDto.toEntity()

        assertThat(result).isEqualTo(expectedEntity)
    }

    private val userListRemoteDto = UserListRemoteDto(
        id = 1,
        name = "My Favorite Movies",
        description = "A collection of the best movies ever.",
        itemCount = 25
    )

    private val expectedEntity = UserList(
        id = 1,
        name = "My Favorite Movies",
        description = "A collection of the best movies ever.",
        itemCount = 25
    )
}