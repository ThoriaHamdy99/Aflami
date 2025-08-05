package com.amsterdam.viewmodel.profile

sealed interface ProfileEffect {
    data object LanguageChanged : ProfileEffect
    data object ThemeChanged : ProfileEffect
    data object LanguageNotChanged : ProfileEffect
    data object ThemeNotChanged : ProfileEffect
    data object NavigateToLogin : ProfileEffect
    data object UserDataNotLoaded : ProfileEffect
}