package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase

interface ProfileInteractionListener {
    fun onClickLogin()

    fun onClickLanguage()
    fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language)
    fun onDismissLanguageDialog()

    fun onClickTheme()
    fun onChangeTheme(isDarkTheme: Boolean)
    fun onDismissThemeDialog()

}