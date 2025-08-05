package com.amsterdam.viewmodel.profile

sealed interface ProfileEffect {
    data class LanguageChanged(val value: String) : ProfileEffect
    data class ThemeChanged(val isDarkTheme: Boolean) : ProfileEffect
    data object NavigateToLogin : ProfileEffect
}