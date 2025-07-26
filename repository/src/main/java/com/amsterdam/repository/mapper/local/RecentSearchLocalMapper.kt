package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.mapper.shared.EntityMapper

class RecentSearchLocalMapper : EntityMapper<LocalSearchDto, String> {
    override fun toEntity(dto: LocalSearchDto): String {
        return dto.searchKeyword
    }
}