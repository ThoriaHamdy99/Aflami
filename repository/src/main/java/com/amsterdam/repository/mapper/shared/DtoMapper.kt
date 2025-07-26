package com.amsterdam.repository.mapper.shared

interface DtoMapper <Entity, Dto>{
    fun toDto(entity: Entity, args: List<Any> = emptyList()): Dto
    fun toDtoList(entityList: List<Entity>, args: List<Any> = emptyList()): List<Dto> =
        entityList.map { toDto(it, args)}
}