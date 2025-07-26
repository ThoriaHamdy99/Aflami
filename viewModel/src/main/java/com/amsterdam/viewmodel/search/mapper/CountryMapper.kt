package com.amsterdam.viewmodel.search.mapper

import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.search.countrySearch.CountryItemUiState

fun Country.toUiState(): CountryItemUiState {
    return CountryItemUiState(
        countryName = this.countryName,
        countryIsoCode = this.countryIsoCode
    )
}

fun List<Country>.toUiState(): List<CountryItemUiState> {
    return this.map { country ->
        country.toUiState()
    }
}

fun CountryItemUiState?.toCountry(): Country {
    return Country(
        countryName = this?.countryName ?: "",
        countryIsoCode = this?.countryIsoCode ?: ""
    )
}
