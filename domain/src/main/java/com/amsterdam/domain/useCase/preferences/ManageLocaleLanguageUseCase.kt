package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun initDeviceLanguage(language: String) {
        preferencesRepository.initDeviceLanguage(language)
    }

    suspend fun setDeviceLanguage(language: String) {
        preferencesRepository.setDeviceLanguage(language)
    }

    fun getDeviceLanguage(): Flow<Language> {
        return preferencesRepository.getDeviceLanguage().map {
            Language.fromLanguage(it)
        }
    }

    enum class Language(val value: String) {
        ENGLISH("en"),
        ARABIC("ar");

        companion object {
            fun fromLanguage(value: String): Language {
                return when (value) {
                    "en" -> ENGLISH
                    "ar" -> ARABIC
                    else -> ENGLISH
                }
            }
            fun toLanguage(languageName: String): Language {
                return when (languageName) {
                    "ENGLISH" -> ENGLISH
                    "ARABIC" -> ARABIC
                    else -> ENGLISH
                }
            }
        }
    }
}