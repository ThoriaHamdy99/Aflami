package com.amsterdam.viewmodel.search.actorSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchUiState.SearchByActorError
import com.amsterdam.viewmodel.search.mapper.toMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchActorViewModel @Inject constructor(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<ActorSearchUiState, SearchActorEffect>(
        ActorSearchUiState(),
    dispatcherProvider,
),
    SearchActorInteractionListener {
    private val keywordFlow = MutableStateFlow("")

    init {
        observeActorSearchQuery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeActorSearchQuery() {
        viewModelScope.launch {
            keywordFlow
                .debounceSearch(::executeActorSearch)
        }
    }

    private fun executeActorSearch(query: String) {
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByActorUseCase(
                                query,
                                page,
                            )
                        }
                    },
                ).flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }.cachedIn(viewModelScope)
            },
            onSuccess = ::handleSearchResults,
            onError = ::onError,
        )
    }

    override fun onUserSearchChange(keyword: String) {
        keywordFlow.update { oldText -> keyword }
        updateState { it.copy(keyword = keyword, isLoading = keyword.isNotBlank()) }
    }

    private fun handleSearchResults(movies: Flow<PagingData<MovieItemUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                isLoading = false,
                error = null,
            )
        }
    }

    override fun onClickNavigateBack() {
        onSaveSearchHistory()
        sendNewEffect(SearchActorEffect.NavigateBack)
    }

    override fun onClickRetrySearch() {
        updateState { it.copy(isLoading = true) }
        executeActorSearch(state.value.keyword)
    }

    override fun onClickMovie(movieId: Long) {
        sendNewEffect(SearchActorEffect.NavigateToDetailsScreen(movieId))
    }

    override fun onSaveSearchHistory() {
        if (state.value.keyword.isBlank()) return
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearchForActor(state.value.keyword) },
            onSuccess = { },
            onError = { },
        )
    }

    private fun onError(aflamiException: AflamiException) {
        updateState {
            when (aflamiException) {
                is NetworkException ->
                    it.copy(
                        error = SearchByActorError.NetworkError,
                        isLoading = false,
                        movies = emptyFlow(),
                    )

                else ->
                    it.copy(
                        isLoading = false,
                        movies = emptyFlow(),
                    )
            }
        }
    }
}
