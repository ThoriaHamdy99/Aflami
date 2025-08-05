package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase

interface ProfileInteractionListener {
    fun onClickLogin()

    fun onClickLanguageSetting()
    fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language)
    fun onApplyLanguage()
    fun onDismissLanguageDialog()

    fun onClickThemeSetting()
    fun onChangeTheme(isDarkTheme: Boolean)
    fun onApplyTheme()
    fun onDismissThemeDialog()

}