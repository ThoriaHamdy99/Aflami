package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel

class SetRestrictionLevelUseCase (
    private val preferencesRepository: AppPreferencesRepository
) {
    suspend operator fun invoke(restrictionLevel: RestrictionLevel){
        preferencesRepository.setRestrictionLevel(restrictionLevel)
    }
}