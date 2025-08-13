package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.SearchLocalDto

fun SearchLocalDto.toEntity(): String =
    searchKeyword

fun List<SearchLocalDto>.toEntityList(): List<String> = map { it.toEntity() }