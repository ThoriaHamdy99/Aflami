package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.useCase.details.GetTvShowsByGenreIdUseCase
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@HiltViewModel
class CategoriesTvShowsDetailsViewModel @Inject constructor(
    private val getTvShowsByGenreIdUseCase: GetTvShowsByGenreIdUseCase,
    private val categoriesTvShowsDetailsArgs: CategoriesTvShowsDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesTvShowsDetailsUiState, CategoriesTvShowsDetailsUiEffect>(
    CategoriesTvShowsDetailsUiState(),
    dispatcherProvider
), CategoriesTvShowsDetailsInteractionListener {
    private val currentGenre = MutableStateFlow<TvShowGenre?>(null)

    init {
        val initialGenre = categoriesTvShowsDetailsArgs.genre
            ?.let { genreStr -> TvShowGenre.entries.find { it.name == genreStr } }
            ?: TvShowGenre.DRAMA
        currentGenre.value = initialGenre

        val pagingFlow = currentGenre
            .flatMapLatest { genre ->
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getTvShowsByGenreIdUseCase(genre!!, page)
                        }
                    }
                ).flow.map { pagingData -> pagingData.map { it.toTvShowUiState() } }
            }
            .cachedIn(viewModelScope)

        updateState {
            it.copy(

                selectedGenreName = initialGenre.name,
                tvShowGenres = it.tvShowGenres.map { genreItem ->
                    genreItem.copy(
                        selectableTvShowGenre = genreItem.selectableTvShowGenre.copy(
                            isSelected = genreItem.selectableTvShowGenre.item == initialGenre
                        )
                    )
                },
                tvShows = pagingFlow


            )
        }
    }

    override fun onBackClicked() {
        sendNewNavigationEffect(CategoriesTvShowsDetailsUiEffect.NavigateBack)
    }

    override fun onTvShowCardClicked(tvShowId: Long) {
        sendNewNavigationEffect(CategoriesTvShowsDetailsUiEffect.NavigateToTvShowDetails(tvShowId))
    }

    override fun onGenreClicked(tvShowGenre: TvShowGenre) {
        currentGenre.value = tvShowGenre
        updateState {
            it.copy(
                selectedGenreName = tvShowGenre.name,
                tvShowGenres = it.tvShowGenres.map { genreItem ->
                    genreItem.copy(
                        selectableTvShowGenre = genreItem.selectableTvShowGenre.copy(
                            isSelected = genreItem.selectableTvShowGenre.item == tvShowGenre
                        )
                    )
                }
            )
        }
    }

    override fun onClickRetryRequest() {
        currentGenre.value?.let {
            currentGenre.value = it
        }
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (loadStates.refresh) {
            is LoadState.Loading -> {
                updateState {
                    it.copy(isLoading = true)
                }
            }

            is LoadState.Error -> {
                updateState {
                    it.copy(isLoading = false)
                }
            }

            is LoadState.NotLoading -> {
                updateState {
                    it.copy(isLoading = false)
                }

            }
        }
    }
}
