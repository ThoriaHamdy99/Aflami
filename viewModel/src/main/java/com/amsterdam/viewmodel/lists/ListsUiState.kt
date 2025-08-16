package com.amsterdam.viewmodel.lists

import com.amsterdam.viewmodel.shared.uiStates.UserListItemUiState
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.viewmodel.shared.uiStates.WishListItemUiState

data class ListsUiState(
    val isLoading: Boolean = true,
    val userLists: List<WishListItemUiState> = emptyList(),
    val errorUiState: ListsErrorState? = null,
    val isCreateNewListDialogVisible: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val listName: String = "",
    val isCreateListLoading: Boolean = false,
)