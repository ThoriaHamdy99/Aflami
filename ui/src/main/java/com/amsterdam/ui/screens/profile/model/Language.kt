package com.amsterdam.ui.screens.profile.model

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.ui.R

enum class Language(
    val nameResourceId: Int,
    val exampleResourceId: Int
) {
    ENGLISH(
        R.string.english,
        R.string.english_example
    ),
    ARABIC(
        R.string.arabic,
        R.string.arabic_example
    );

    companion object {
        fun fromUiState(language: ManageLocaleLanguageUseCase.Language): Language {
            return when (language) {
                ManageLocaleLanguageUseCase.Language.ENGLISH -> ENGLISH
                ManageLocaleLanguageUseCase.Language.ARABIC -> ARABIC
            }
        }
    }
}