package com.amsterdam.viewmodel.lists

import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState

data class ListsUiState(
    val isLoading: Boolean = true,
    val userLists: List<UserListItemUiState> = emptyList(),
    val isCreateNewListDialogVisible: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val listName: String = "",
    val isCreateListLoading: Boolean = false,
)