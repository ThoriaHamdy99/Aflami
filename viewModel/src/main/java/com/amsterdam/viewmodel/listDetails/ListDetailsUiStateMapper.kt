package com.amsterdam.viewmodel.listDetails

import android.annotation.SuppressLint
import com.amsterdam.entity.UserListItem
import com.amsterdam.viewmodel.shared.uiStates.media.MediaItemUiState
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType
import javax.inject.Inject

class ListDetailsUiStateMapper @Inject constructor() {
    @SuppressLint("DefaultLocale")
    fun listItemToMediaItemUiState(userListItem: UserListItem): MediaItemUiState {
        val mediaType = when (userListItem.mediaType) {
            UserListItem.MediaType.MOVIE -> MediaType.MOVIE
            UserListItem.MediaType.TV_SHOW -> MediaType.TV_SHOW
        }
        return MediaItemUiState(
            id = userListItem.id,
            name = userListItem.title,
            rate = String.format("%.1f", userListItem.rating),
            posterImageUrl = userListItem.posterUrl,
            yearOfRelease = userListItem.releaseDate.year.toString(),
            mediaType = mediaType
        )
    }
}