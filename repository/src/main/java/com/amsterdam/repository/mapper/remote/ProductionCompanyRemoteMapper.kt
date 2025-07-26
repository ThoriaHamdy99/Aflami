package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.repository.dto.remote.ProductionCompanyDto
import com.amsterdam.repository.mapper.shared.EntityMapper

class ProductionCompanyRemoteMapper :
    EntityMapper<ProductionCompanyDto, ProductionCompany> {
    override fun toEntity(dto: ProductionCompanyDto): ProductionCompany {
        return ProductionCompany(
            id = dto.id,
            imageUrl = dto.fullLogoPath.orEmpty(),
            name = dto.name,
            country = dto.originCountry
        )
    }
}