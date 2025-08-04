package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow

class ManageAppThemeUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun setAppTheme(theme: Boolean) {
        preferencesRepository.setAppTheme(theme)
    }

    fun getAppTheme(): Flow<Boolean> {
        return preferencesRepository.getAppTheme()
    }

    suspend fun initAppTheme(theme: Boolean) {
        preferencesRepository.initAppTheme(theme)
    }
}