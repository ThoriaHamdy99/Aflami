package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.profile.model.Language

@Composable
fun LanguageDialog(
    language: Language,
    modifier: Modifier = Modifier,
    onConfirm: (language: Language) -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismiss = onDismiss,
        isDismissible = true,
        modifier = modifier,
    ) {
        DialogContent(
            language = language,
            onConfirm = { onConfirm(language) },
            onDismiss = onDismiss
        )
    }
}


@Composable
fun DialogContent(
    language: Language,
    onConfirm: (language: Language) -> Unit,
    onDismiss: () -> Unit
) {
    val updatedLanguage by remember { mutableStateOf(language) }

    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.language),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title
            )
            IconButton(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                contentDescription = stringResource(R.string.cancel),
                onClick = {
                    onDismiss()
                },
                tint = AppTheme.color.title,
            )
        }

        SelectionContainer {
            Row {
                Text(
                    text = updatedLanguage.name,
                    style = AppTheme.textStyle.body.medium,
                    color = AppTheme.color.title,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
        ConfirmButton(
            title = stringResource(R.string.apply),
            onClick = { onConfirm(updatedLanguage) },
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}