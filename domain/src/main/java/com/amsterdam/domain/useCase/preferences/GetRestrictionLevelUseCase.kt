package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow

class GetRestrictionLevelUseCase(
    private val preferencesRepository: AppPreferencesRepository
) {
    operator fun invoke(): Flow<RestrictionLevel>{
        return preferencesRepository.getRestrictionLevel()
    }
}