package com.amsterdam.viewmodel.categoriesDetails

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.details.GetMoviesByGenreIdUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@HiltViewModel
class CategoriesMoviesDetailsViewModel @Inject constructor(
    private val getMoviesByGenreIdUseCase: GetMoviesByGenreIdUseCase,
    private val categoriesMovieDetailsArgs: CategoriesMovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesMoviesDetailsUiState, CategoriesMoviesDetailsUiEffect>(
    CategoriesMoviesDetailsUiState(),
    dispatcherProvider
), CategoriesMoviesDetailsInteractionListener {

    private val currentGenre = MutableStateFlow<MovieGenre?>(null)

    init {
        val initialGenre = MovieGenre.valueOf(categoriesMovieDetailsArgs.genre!!)
        currentGenre.value = initialGenre

        val pagingFlow = currentGenre
            .flatMapLatest { genre ->
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByGenreIdUseCase(genre!!, page)
                        }
                    }
                ).flow.map { pagingData -> pagingData.map { it.toMovieUiState() } }
            }
            .cachedIn(viewModelScope)

        updateState {
            it.copy(
                movies = pagingFlow,
                selectedGenreName = initialGenre.name,
                movieGenres = it.movieGenres.map { genreItem ->
                    genreItem.copy(
                        selectableMovieGenre = genreItem.selectableMovieGenre.copy(
                            isSelected = genreItem.selectableMovieGenre.item == initialGenre
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
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> updateState { it.copy(isLoading = true, errorUiState = null) }
            is LoadState.NotLoading -> updateState { it.copy(isLoading = false) }
            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                onFetchError(refreshState.error as AflamiException)
            }
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState {
            it.copy(
                errorUiState = CategoriesMoviesDetailsUiState.CategoriesDetailsErrorState
                    .toSearchErrorState(exception),
                isLoading = false
            )
        }
    }

    override fun onBackClicked() {
        sendNewNavigationEffect(CategoriesMoviesDetailsUiEffect.NavigateBack)
    }

    override fun onMovieCardClicked(movieId: Long) {
        sendNewNavigationEffect(CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails(movieId))
    }

    override fun onGenreClicked(movieGenre: MovieGenre) {
        currentGenre.value = movieGenre
        updateState {
            it.copy(
                selectedGenreName = movieGenre.name,
                movieGenres = it.movieGenres.map { genreItem ->
                    genreItem.copy(
                        selectableMovieGenre = genreItem.selectableMovieGenre.copy(
                            isSelected = genreItem.selectableMovieGenre.item == movieGenre
                        )
                    )
                }
            )
        }
    }
}
