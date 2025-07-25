package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCompanyResponse(
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>
)
