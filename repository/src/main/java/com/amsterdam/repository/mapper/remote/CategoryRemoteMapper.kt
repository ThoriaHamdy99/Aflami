package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Category
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.mapper.shared.EntityMapper

class CategoryRemoteMapper : EntityMapper<RemoteCategoryDto, Category> {
    override fun toEntity(dto: RemoteCategoryDto): Category {
        return Category(
            id = dto.id.toLong(),
            name = dto.name,
            imageUrl = ""
        )
    }
}