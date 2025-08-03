package com.amsterdam.entity

import kotlinx.datetime.LocalDate

data class UserListItem(
    val id: Long,
    val title: String,
    val description: String,
    val mediaType: MediaType,
    val rating: Float,
    val releaseDate: LocalDate,
    val posterUrl: String
) {
    enum class MediaType {
        MOVIE,
        TV_SHOW
    }
}
