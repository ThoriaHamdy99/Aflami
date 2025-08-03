package com.amsterdam.viewmodel.listDetails

import androidx.paging.PagingData
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ListDetailsUiState(
    val isLoading: Boolean = true,
    val listId: Long = 0,
    val listName: String = "",
    val listItems: Flow<PagingData<MediaItemUiState>> = emptyFlow()
)