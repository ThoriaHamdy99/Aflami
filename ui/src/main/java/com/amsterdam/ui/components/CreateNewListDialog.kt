package com.amsterdam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun CreateNewListDialog(
    modifier: Modifier = Modifier,
    onCreateListClick: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    var newListName by remember { mutableStateOf("") }
    Dialog(
        modifier = modifier,
        onDismiss = onDismiss,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            DialogHeaderSection(
                title = stringResource(R.string.create_list),
                onDismiss = onDismiss,
            )
            TextField(
                text = newListName,
                onValueChange = {
                    newListName = it
                },
                hintText = stringResource(R.string.my_favorite),
                leadingIcon = com.amsterdam.designsystem.R.drawable.ic_nav_lists,
            )

            ConfirmButton(
                title = stringResource(R.string.create),
                isEnabled = true,
                isLoading = false,
                isNegative = false,
                onClick = {
                    if (newListName.isEmpty()) return@ConfirmButton
                    onCreateListClick(newListName)
                },
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun PreviewCreateNewListDialog() {
    CreateNewListDialog()
}
