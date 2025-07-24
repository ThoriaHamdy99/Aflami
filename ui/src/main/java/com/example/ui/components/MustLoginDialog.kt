package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.designsystem.components.IconButton
import com.example.designsystem.components.Text
import com.example.designsystem.components.buttons.OutlinedButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.designsystem.utils.modifierExtensions.dropShadow
import com.example.ui.R

@Composable
fun MustLoginDialog(
    title: String,
    onDismiss: () -> Unit,
    onLoginClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
            ),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    AppTheme.color.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title
                )
                IconButton(
                    painter = painterResource(com.example.designsystem.R.drawable.ic_cancel),
                    contentDescription = null,
                    onClick = {
                        onDismiss()
                    },
                    tint = AppTheme.color.title,
                )
            }

            Image(
                painter = painterResource(R.drawable.img_empty_user_pic),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp, AppTheme.color.stroke,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .dropShadow(
                        blur = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = AppTheme.color.droppedShadowColor
                    )
            )
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = "Please login to access your account details and other features!",
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center

            )
            OutlinedButton(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                title = "Login",
                onClick = { onLoginClicked() },
                isEnabled = true,
                isLoading = false,
                isNegative = false,
            )
        }
    }
}

@Composable
@ThemeAndLocalePreviews
fun CustomDialogPreview() {
    AflamiTheme {
        MustLoginDialog(
            onDismiss = { },
            title = "Rate",
            onLoginClicked = { }
        )

    }
}