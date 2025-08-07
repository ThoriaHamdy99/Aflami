package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.ProfileDao
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import javax.inject.Inject

class ProfileLocalDataSourceImpl @Inject constructor(
    private val profileDao: ProfileDao
) : ProfileLocalDataSource {
    override suspend fun getAccountDetails(): AccountDetailsLocalDto? {
        return profileDao.getAccountDetails()
    }

    override suspend fun addAccountDetails(accountDetails: AccountDetailsLocalDto) {
        profileDao.upsertAccountDetails(accountDetails)
    }

    override suspend fun deleteAccountDetails() {
        profileDao.deleteAllSearches()
    }

}