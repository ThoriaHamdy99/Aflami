package com.example.localdatasource.dataStore.datasource

import com.example.localdatasource.dataStore.appPreferences.AppPreferences
import com.example.repository.datasource.local.AuthenticationLocalSource

class AuthenticationLocalDataSourceImpl(
    private val appPreferences: AppPreferences
): AuthenticationLocalSource {
    override suspend fun setLoginType(loginType: String) {
        appPreferences.setLoginType(loginType)
    }

    override suspend fun getLoginType(): String {
        return appPreferences.getLoginType()
    }
}