package com.amsterdam.viewmodel.search.keywordSearch

import com.amsterdam.viewmodel.shared.BaseViewModel

sealed interface SearchUiEffect: BaseViewModel.BaseUiEffect {
    object NavigateToWorldSearch : SearchUiEffect
    object NavigateToActorSearch : SearchUiEffect
    data class NavigateToMovieDetails(val movieId: Long) : SearchUiEffect
    data class NavigateToTvShowDetails(val tvShowId: Long) : SearchUiEffect
    object NavigateBack : SearchUiEffect
}