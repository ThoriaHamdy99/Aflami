package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.logger.Loggable
import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val profileApiService: ProfileApiService
) : ProfileRemoteDataSource, Loggable {
    override suspend fun getAccountDetails(): AccountDetailsRemoteDto {
        return responseCall(logger = logger, execute = { profileApiService.getAccountDetails() })
    }
}