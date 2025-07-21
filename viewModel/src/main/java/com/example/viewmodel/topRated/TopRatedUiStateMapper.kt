package com.example.viewmodel.topRated

import android.annotation.SuppressLint
import com.example.entity.Movie
import com.example.viewmodel.home.HomeUiState.TopRatedMovieItemUiState

class TopRatedUiStateMapper {
    @SuppressLint("DefaultLocale")
    fun toUiState(topRatedMovies: List<Movie>): TopRatedUiState {
        return TopRatedUiState(
            topRatedMovies = topRatedMovies.map { movie ->
                TopRatedMovieItemUiState(
                    id = movie.id,
                    name = movie.name,
                    rating = String.format("%.1f", movie.rating),
                    posterImageUrl = movie.posterUrl,
                    yearOfRelease = movie.productionYear.toString()
                )
            }
        )
    }
}