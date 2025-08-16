package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.IconButton
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.movieDetails.UserListUiState


@Composable
fun RemoveFromListsDialog(
    userLists: List<UserListUiState>,
    modifier: Modifier = Modifier,
    selectedLists: List<UserListUiState> = emptyList(),
    isRemoveMovieFromListLoading: Boolean = false,
    onSelectedListChange: (List<UserListUiState>) -> Unit = {},
    onRemoveFromSelectedList: (List<Long>) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismiss = onDismiss,
        scrollable = false,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            DialogHeaderSection(
                onDismiss = onDismiss,
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
            ) {
                item {
                    Text(
                        text = stringResource(R.string.remove_from_list_description),
                        style = AppTheme.textStyle.body.small,
                        color = AppTheme.color.body
                    )
                }
                items(userLists) { userList ->
                    SelectionListItem(
                        listName = userList.name,
                        itemCount = userList.itemCount,
                        isSelected = selectedLists.contains(userList),
                        onSelectItem = {
                            val updatedSelection = if (selectedLists.contains(userList)) {
                                selectedLists - userList
                            } else {
                                selectedLists + userList
                            }
                            onSelectedListChange(updatedSelection)
                        },
                    )
                }
            }

            ConfirmButton(
                title = stringResource(R.string.remove),
                onClick = { onRemoveFromSelectedList(selectedLists.map { it.id }) },
                isEnabled = selectedLists.isNotEmpty(),
                isLoading = isRemoveMovieFromListLoading,
                isNegative = false,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun SelectionListItem(
    listName: String,
    itemCount: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelectItem: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isSelected) Color.Transparent else AppTheme.color.stroke,
                    shape = RoundedCornerShape(16.dp),
                )
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSelected) AppTheme.color.primaryVariant else AppTheme.color.surface)
                .clickable(onClick = onSelectItem)
                .padding(horizontal = 12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(vertical = 7.dp),
        ) {
            Text(
                text = listName,
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.body,
            )

            val listItemCount = pluralStringResource(R.plurals.item_count, itemCount, itemCount)
            Text(
                text = listItemCount,
                style = AppTheme.textStyle.label.small,
                color = AppTheme.color.hint,
            )
        }

        val icon =
            if (isSelected) {
                com.amsterdam.designsystem.R.drawable.ic_checkmark_circle
            } else {
                com.amsterdam.designsystem.R.drawable.ic_cancel_circle
            }

        Icon(
            painter =
                painterResource(icon),
            contentDescription = null,
            tint = if (isSelected) AppTheme.color.primary else AppTheme.color.hint,
        )
    }
}

@Composable
private fun DialogHeaderSection(onDismiss: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.remove_from_list),
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title,
        )

        IconButton(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
            contentDescription = null,
            tint = AppTheme.color.title,
            onClick = onDismiss,
        )
    }
}