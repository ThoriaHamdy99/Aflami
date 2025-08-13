package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.UserList
import com.amsterdam.repository.dto.remote.UserListRemoteDto

fun UserListRemoteDto.toUserList(): UserList {
    return UserList(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 