package com.amsterdam.ui.screens.profile

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.TopAppBar
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.components.divider.HorizontalDivider
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.modifierExtensions.dropShadow
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.profile.ProfileEffect
import com.amsterdam.viewmodel.profile.ProfileInteractionListener
import com.amsterdam.viewmodel.profile.ProfileUiState
import com.amsterdam.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateToLogin -> navController.navigate(Route.Login)
            }
        }
    }
    ProfileScreenContent(
        state,
        interactionListener = viewModel
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileUiState,
    interactionListener: ProfileInteractionListener
) {
    val animationDuration by remember { mutableIntStateOf(1000) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets(bottom = LocalScaffoldBottomPadding.current)),
        contentAlignment = Alignment.TopStart
    ) {
        AnimatedVisibility(
            state.isLoading,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingContainer()
            }
        }

        AnimatedVisibility(
            state.isUserLoggedIn,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            LoggedInContent()
        }

        AnimatedVisibility(
            !state.isUserLoggedIn,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            NotLoggedInContent(
                interactionListener::onClickLogin
            )
        }
    }
}

@Composable
private fun NotLoggedInContent(onClickLogin: () -> Unit) {
    var topBarHeight by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile),
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .onSizeChanged {
                            topBarHeight = it.height
                        }
                )
            },
        )
        LoginSection(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .padding(horizontal = 48.dp),
            unneededSpace = topBarHeight.dp,
            onClickLogin = onClickLogin
        )
    }
}

@Composable
private fun LoggedInContent() {
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
            item { ProfileImageSection() }
            item { ProfileInfoSection() }
            item { HistoryAndRatingSection() }
            item { HorizontalDivider() }
            item { SettingsSection() }
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
    }
}

@Composable
private fun SettingsSection() {
    CustomSettingCard(
        modifier = Modifier.padding(top = 24.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_language,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.language),
        endText = "ENG",
    )

    CustomSettingCard(
        modifier = Modifier.padding(top = 8.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_moon,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.app_theme),
        endText = "Dark",
    )

    CustomSettingCard(
        modifier = Modifier.padding(top = 8.dp),
        startIconResourceId = com.amsterdam.designsystem.R.drawable.ic_settings,
        endIconResourceId = com.amsterdam.designsystem.R.drawable.ic_arrow_right,
        startText = stringResource(R.string.settings)
    )
}

@Composable
fun HistoryAndRatingSection() {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CustomCard(
            imageResourceId = R.drawable.img_user_history,
            text = stringResource(R.string.watch_history)
        )
        CustomCard(
            imageResourceId = com.amsterdam.designsystem.R.drawable.img_user_rating,
            text = stringResource(R.string.my_rating)
        )
    }
}

@Composable
fun ProfileInfoSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "@Hamsa_Ali_4",
            style = AppTheme.textStyle.label.medium,
            color = AppTheme.color.body,
            modifier = Modifier
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = AppTheme.color.pointsOverlayGradient
                    ),
                    shape = CircleShape
                )
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(
                text = "325 Pts.",
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.onPrimary,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun ProfileImageSection() {
    Box {
        Image(
            painter = painterResource(R.drawable.profile_background_image),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .padding(bottom = 27.dp)
                .fillMaxWidth()
                .height(211.dp)
                .dropShadow(
                    blur = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = AppTheme.color.profileOverlay
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppTheme.color.surface,
                            AppTheme.color.surface
                        )
                    )
                ),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(R.drawable.img_empty_user_pic),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 46.dp)
                .size(96.dp)
                .dropShadow(
                    blur = 12.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = AppTheme.color.droppedShadowColor
                )
                .clip(shape = RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp, AppTheme.color.stroke,
                    shape = RoundedCornerShape(24.dp)
                )
        )
    }
}

@Composable
private fun CustomSettingCard(
    @DrawableRes startIconResourceId: Int,
    @DrawableRes endIconResourceId: Int,
    startText: String,
    modifier: Modifier = Modifier,
    endText: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(12.dp)
                )
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(startIconResourceId),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(24.dp),
                colorFilter = ColorFilter.tint(AppTheme.color.body)
            )
        }

        Text(
            text = startText,
            style = AppTheme.textStyle.label.large,
            color = AppTheme.color.title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
        )

        if (endText.isNotBlank()) {
            Text(
                text = endText,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.body,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            )
        }

        Image(
            painter = painterResource(endIconResourceId),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .size(20.dp),
            colorFilter = ColorFilter.tint(AppTheme.color.hint)
        )

    }
}

@Composable
private fun RowScope.CustomCard(
    @DrawableRes imageResourceId: Int,
    text: String
) {
    Box(
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(71.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = text,
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.title,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            )
        }
        Image(
            painter = painterResource(imageResourceId),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .size(width = 64.dp, height = 71.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun LoginSection(
    modifier: Modifier = Modifier,
    unneededSpace: Dp,
    onClickLogin: () -> Unit
) {
    CenterOfScreenContainer(
        unneededSpace = unneededSpace,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_empty_user_pic),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(80.dp)
                    .dropShadow(
                        blur = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = AppTheme.color.droppedShadowColor
                    )
                    .clip(shape = RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp, AppTheme.color.stroke,
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            Text(
                text = stringResource(R.string.login_message),
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(95.dp),
                title = stringResource(R.string.login),
                onClick = onClickLogin,
                isEnabled = true,
                isLoading = false,
                isNegative = false
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ProfileScreenPreview() {
    ProfileScreenContent(
        ProfileUiState(),
        interactionListener = object : ProfileInteractionListener {
            override fun onClickLogin() {}
        }
    )
}