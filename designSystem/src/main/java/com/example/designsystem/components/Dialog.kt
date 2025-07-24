package com.example.designsystem.components

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColor
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@SuppressLint("ContextCastToActivity")
@Composable
fun Dialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isDismissible: Boolean = true,
    behindDialogColor: Color = AppTheme.color.dialogBackground,
    content: @Composable () -> Unit,
) {
    val activity: Activity = LocalContext.current as Activity

    val statusBarColor by remember {
        mutableIntStateOf(
            activity.window.statusBarColor.toColor().toArgb()
        )
    }
    val navigationBarColor by remember {
        mutableIntStateOf(
            activity.window.navigationBarColor.toColor().toArgb()
        )
    }

    DisposableEffect(key1 = statusBarColor) {
        onDispose {
            activity.window.statusBarColor = statusBarColor
            activity.window.navigationBarColor = navigationBarColor
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = isDismissible,
            dismissOnClickOutside = isDismissible,

        ),
    ) {
        activity.window.navigationBarColor = behindDialogColor.toArgb()
        activity.window.statusBarColor = behindDialogColor.toArgb()
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = behindDialogColor),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
@ThemeAndLocalePreviews
fun DialogPreview() {
    AflamiTheme {
        Dialog(
            content = {},
            onDismiss = {},
            isDismissible = true,
            behindDialogColor = AppTheme.color.dialogBackground
        )
    }
}