package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.PeopleApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.PeopleRemoteDataSource
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto
import com.amsterdam.repository.dto.remote.RemotePeopleResponse
import javax.inject.Inject

class PeopleRemoteDataSourceImpl @Inject constructor(
    private val peopleApiService: PeopleApiService
) : PeopleRemoteDataSource {

    override suspend fun getRandomizedTrendingPeople(requiredNumber: Int): List<RemotePeopleItemDto> {
        val totalPages = getTrendingPeople(FIRST_PAGE).totalPages

        return (FIRST_PAGE..totalPages)
                .shuffled()
                .fold(emptyList()) { accumulatedPeople, page ->
                    accumulatedPeople.takeIf { it.size >= requiredNumber }
                    ?: getTrendingPeople(page).results
                            .filter(::onFilterHighQualityPeopleData)
                            .shuffled()
                            .distinctBy(RemotePeopleItemDto::id)
                            .plus(accumulatedPeople)
                            .take(requiredNumber)
                }
    }

    private fun onFilterHighQualityPeopleData(people: RemotePeopleItemDto): Boolean {
        return people.name.isNotBlank() && !people.profilePath.isNullOrBlank()
    }

    private suspend fun getTrendingPeople(page: Int): RemotePeopleResponse {
        return responseCall(
            execute = {
                peopleApiService.getTrendingPeople(page = page)
            }
        )
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}