package com.amsterdam.viewmodel.cast

data class CastUiState(
    val cast: List<ActorUiState> = emptyList(),
    val isLoading: Boolean = false,
    val isRetryLoading : Boolean = false
) {
    data class ActorUiState(
        val actorImage: String = "",
        val actorName: String = ""
    )
}