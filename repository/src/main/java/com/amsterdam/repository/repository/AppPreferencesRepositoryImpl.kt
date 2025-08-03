package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.mapper.local.RestrictionLevelMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences,
    private val restrictionLevelMapper: RestrictionLevelMapper
) : AppPreferencesRepository {
    override fun getDeviceLanguage(): Flow<String> = preferences.getDeviceLanguage()

    override suspend fun setDeviceLanguage(language: String) =
        preferences.setDeviceLanguage(language)

    override fun getRestrictionLevel(): Flow<RestrictionLevel> {
        return restrictionLevelMapper.fromLocalRestrictionLevel(preferences.getRestrictionLevel())
    }

    override suspend fun setRestrictionLevel(restrictionLevel: RestrictionLevel) {
        preferences.setRestrictionLevel(restrictionLevelMapper.toLocalRestrictionLevel(restrictionLevel))
    }
}