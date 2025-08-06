package com.amsterdam.viewmodel.shared.mappers

import com.amsterdam.entity.ProductionCompany
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ProductionCompanyUiState

fun ProductionCompany.toUiState(): ProductionCompanyUiState {
    return ProductionCompanyUiState(
        image = imageUrl,
        name = name,
        country = country
    )
}