package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ManageLocaleLanguageUseCase(
    private val preferencesRepository: AppPreferencesRepository,
) {
    suspend fun initAppLanguage(language: String) {
        preferencesRepository.initAppLanguage(language)
    }

    suspend fun setAppLanguage(language: String) {
        preferencesRepository.setAppLanguage(language)
    }

    fun getAppLanguage(): Flow<Language> {
        return preferencesRepository.getAppLanguage().map {
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