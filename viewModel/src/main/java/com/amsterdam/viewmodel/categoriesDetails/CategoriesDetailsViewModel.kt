package com.amsterdam.viewmodel.categoriesDetails

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.details.GetMoviesByGenreIdUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CategoriesDetailsViewModel @Inject constructor(
    private val getMoviesByGenreIdUseCase: GetMoviesByGenreIdUseCase,
    private val categoriesMovieDetailsArgs: CategoriesMovieDetailsArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CategoriesDetailsUiState, CategoriesDetailsUiEffect>(
    CategoriesDetailsUiState(),
    dispatcherProvider
), CategoriesDetailsInteractionListener {
    init {
        val mediaType = categoriesMovieDetailsArgs.mediaType
        val initialGenre =
            MovieGenre.valueOf(categoriesMovieDetailsArgs.genre!!) // تحويل من String إلى MovieGenre
        viewModelScope.launch {
            updateState { state ->
                state.copy(
                    appBarTitle = mediaType.toString(),
                    movieGenres = state.movieGenres.map { genreItem ->
                        genreItem.copy(
                            selectableMovieGenre = genreItem.selectableMovieGenre.copy(
                                isSelected = genreItem.selectableMovieGenre.item == initialGenre
                            )
                        )
                    }
                )
            }
            getMovieByGenre(initialGenre, 1)
        }
    }



    override fun onClickRetryRequest(movieGenre: MovieGenre, page: Int) {
        getMovieByGenre(movieGenre, page)
    }


    private fun onFetchError(exception: AflamiException) {
        updateState {
            it.copy(
                errorUiState = CategoriesDetailsUiState.CategoriesDetailsErrorState.toSearchErrorState(
                    exception
                ), isLoading = false
            )
        }
    }


    override fun onBackClicked() {
        sendNewNavigationEffect(CategoriesDetailsUiEffect.NavigateBack)
    }


    override fun onMediaClicked(mediaId: Long) {
        sendNewNavigationEffect(CategoriesDetailsUiEffect.NavigateToMovieDetails(mediaId))


    }

    override fun onTvGenreClicked(tvGenre: TvShowGenre) {
    }


    private fun getMovieByGenre(genre: MovieGenre, page: Int) {

        tryToExecute(
            action = { getMoviesByGenreIdUseCase(genre, page) },
            onSuccess = { movies ->
                updateState {
                    it.copy(
                        selectedGenreName = genre.name,
                        movies = movies.map { movie -> movie.toMovieUiState() },
                        isLoading = false
                    )
                }
            },
            onError = ::onFetchError
        )
    }

    override fun onMovieGenreClicked(movieGenre: MovieGenre) {

        tryToExecute(
            action = { getMoviesByGenreIdUseCase(movieGenre, 1) },
            onSuccess = { movies ->
                onGetMoviesByGenreSuccess(movieGenre, movies)
            },
            onError = ::onFetchError
        )
    }

    private fun onGetMoviesByGenreSuccess(movieGenre: MovieGenre, movies: List<Movie>) {
        updateState { currentState ->
            currentState.copy(
                movieGenres = currentState.movieGenres.map { genreItem ->
                    genreItem.copy(
                        selectableMovieGenre = genreItem.selectableMovieGenre.copy(
                            isSelected = genreItem.selectableMovieGenre.item == movieGenre
                        )
                    )
                },
                selectedGenreName = movieGenre.name,
                movies = movies.map { it.toMovieUiState() },
            )
        }
    }
}