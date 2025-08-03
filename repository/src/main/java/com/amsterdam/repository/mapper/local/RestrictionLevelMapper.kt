package com.amsterdam.repository.mapper.local

import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RestrictionLevelMapper @Inject constructor() {
    fun toLocalRestrictionLevel(restrictionLevel: RestrictionLevel): String {
        return when (restrictionLevel) {
            RestrictionLevel.STRICT -> STRICT
            RestrictionLevel.MODERATE -> MODERATE
            RestrictionLevel.OFF -> OFF
        }
    }

    fun fromLocalRestrictionLevel(restrictionLevel: Flow<String>): Flow<RestrictionLevel> {
        return flow {
            restrictionLevel.collect {
                emit(
                    when(it){
                        STRICT -> RestrictionLevel.STRICT
                        MODERATE -> RestrictionLevel.MODERATE
                        else -> RestrictionLevel.OFF
                    }
                )
            }
        }

    }

    private companion object {
        private const val STRICT = "STRICT"
        private const val MODERATE = "MODERATE"
        private const val OFF = "OFF"
    }
}