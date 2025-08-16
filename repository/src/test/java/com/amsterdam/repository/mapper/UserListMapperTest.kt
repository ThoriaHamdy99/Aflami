package com.amsterdam.repository.mapper

import com.amsterdam.entity.UserList
import com.amsterdam.repository.dto.remote.UserListRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UserListMapperTest {
    private val userListRemoteDto = UserListRemoteDto(
        id = 1,
        name = "My Favorite Movies",
        description = "A collection of the best movies ever.",
        itemCount = 25
    )

    @Nested
    inner class ToEntityTest {
        @Test
        fun `toEntity should map UserListRemoteDto to UserList entity`() {
            val expectedEntity = UserList(
                id = 1,
                name = "My Favorite Movies",
                description = "A collection of the best movies ever.",
                itemCount = 25
            )

            val result = userListRemoteDto.toEntity()

            assertThat(result).isEqualTo(expectedEntity)
        }
    }
}