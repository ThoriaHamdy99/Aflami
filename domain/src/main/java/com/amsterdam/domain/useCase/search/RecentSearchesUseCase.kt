package com.amsterdam.domain.useCase.search

import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.entity.Country
import javax.inject.Inject

class RecentSearchesUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository
) {
    suspend fun addRecentSearch(keyword: String) {
        keyword.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearch(it) }
    }

    suspend fun addRecentSearchForCountry(country: Country) {
        country.countryIsoCode.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearchForCountry(it) }
    }

    suspend fun addRecentSearchForActor(actorName: String) {
        actorName.takeIf { it.isNotBlank() }
            ?.let { recentSearchRepository.addRecentSearchForActor(it) }
    }

    suspend fun getRecentSearches(): List<String> {
        return recentSearchRepository.getAllRecentSearches()
    }

    suspend fun deleteRecentSearch(searchKeyword: String) {
        recentSearchRepository.deleteRecentSearch(searchKeyword = searchKeyword)
    }

    suspend fun deleteRecentSearches() {
        recentSearchRepository.deleteAllRecentSearches()
    }
}