package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language

data class ProfileUiState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfoUiState = UserInfoUiState(),
    val language: Language = Language.ENGLISH,
    val isDarkTheme: Boolean = false,
    val updatedLanguage: Language = Language.ENGLISH,
    val updatedIsDarkTheme: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val showThemeDialog: Boolean = false,
    val error: ProfileError? = null
) {
    data class UserInfoUiState(
        val username: String = "",
        val userAvatarUrl: String = "",
        val userPoints: Int = 0
    )

    sealed class ProfileError {
        data object NetworkError : ProfileError()
    }
}
