package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import javax.inject.Inject

class GetSuggestedCountriesUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {

    suspend operator fun invoke(keyword: String): List<Country> {
        return countryRepository.getCountries()
            .filter {
                it.countryName.contains(keyword, ignoreCase = true)
            }
    }
}