package com.amsterdam.repository.mapper.shared

interface EntityMapper<Dto, Entity> {
    fun toEntity(dto: Dto): Entity
    fun toEntityList(dtoList: List<Dto>): List<Entity> = dtoList.map (::toEntity)
}