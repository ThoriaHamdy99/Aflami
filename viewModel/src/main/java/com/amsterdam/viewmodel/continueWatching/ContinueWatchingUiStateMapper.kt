package com.amsterdam.viewmodel.continueWatching

import android.annotation.SuppressLint
import com.amsterdam.entity.Movie
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import javax.inject.Inject


class ContinueWatchingUiStateMapper @Inject constructor() {
    @SuppressLint("DefaultLocale")
    fun toUiState(continueWatchingMovies: List<Movie>): ContinueWatchingUiState {
        return ContinueWatchingUiState(
            continueWatchingMovies = continueWatchingMovies.map { movie ->
                MovieItemUiState(
                    id = movie.id,
                    name = movie.name,
                    rate = String.format("%.1f", movie.rating),
                    posterImageUrl = movie.posterUrl,
                    yearOfRelease = movie.releaseDate.toString()
                )
            }
        )
    }
}