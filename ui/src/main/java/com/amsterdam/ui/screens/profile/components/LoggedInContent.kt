package com.amsterdam.ui.screens.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.TopAppBar
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.profile.model.Language
import com.amsterdam.viewmodel.profile.ProfileInteractionListener
import com.amsterdam.viewmodel.profile.ProfileUiState


@Composable
fun LoggedInContent(state: ProfileUiState, interactionListener: ProfileInteractionListener) {
    val lazyListState = rememberLazyListState()
    val scrollOffset = remember {
        derivedStateOf { lazyListState.firstVisibleItemScrollOffset }
    }
    val appBarColor by animateColorAsState(
        targetValue = if (scrollOffset.value > 8) AppTheme.color.surface else Color.Transparent,
        animationSpec = tween(800),
        label = "AppBarScrollColor"
    )
    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            item { ProfileImageSection(state.userInfo.userAvatarUrl) }
            item { ProfileInfoSection(state.userInfo.username, state.userInfo.userPoints) }
            item { HistoryAndRatingSection() }
            item { HorizontalDivider() }
            item { SettingsSection(state, interactionListener) }
            item {
                Text(
                    text = "v 1.1",
                    style = AppTheme.textStyle.label.small,
                    color = AppTheme.color.hint,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile),
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                )
            },
            modifier = Modifier
                .background(appBarColor)
        )
        AnimatedVisibility(
            state.showLanguageDialog,
            enter = fadeIn(tween(2000)),
            exit = fadeOut(tween(2000)),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LanguageDialog(
                    language = Language.fromUiState(state.language),
                    onConfirm = {
                        interactionListener.onChangeLanguage(
                            ManageLocaleLanguageUseCase.Language.toLanguage(
                                it.name
                            )
                        )
                    },
                    onDismiss = { interactionListener.onDismissLanguageDialog() }
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LoggedInContentPreview() {
    AflamiTheme {
        LoggedInContent(ProfileUiState(), object : ProfileInteractionListener {
            override fun onClickLogin() {}
            override fun onClickLanguage() {
            }

            override fun onChangeLanguage(language: ManageLocaleLanguageUseCase.Language) {
            }

            override fun onDismissLanguageDialog() {
            }

            override fun onClickTheme() {
            }

            override fun onChangeTheme(isDarkTheme: Boolean) {
            }

            override fun onDismissThemeDialog() {
            }
        })
    }
}