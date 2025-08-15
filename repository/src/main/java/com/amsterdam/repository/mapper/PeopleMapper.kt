package com.amsterdam.repository.mapper

import com.amsterdam.entity.Character
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto

fun RemotePeopleItemDto.toEntity() = Character(
    id = this.id.toLong(),
    name = this.name,
    imageUrl = this.fullPosterUrl.orEmpty()
)

fun List<RemotePeopleItemDto>.toEntityList() = this.map(RemotePeopleItemDto::toEntity)