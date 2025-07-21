package com.example.viewmodel.home

import android.annotation.SuppressLint
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.viewmodel.home.HomeUiState.TopRatedMovieItemUiState

class HomeUiStateMapper {
        @SuppressLint("DefaultLocale")
        fun toUiState(homeScreenData: GetHomeScreenDataUseCase.HomeScreenData): HomeUiState {
            return HomeUiState(
                popularMovies = homeScreenData.popularMovies.map { movie ->
                    HomeUiState.PopularMovieItemUiState(
                        name = movie.name,
                        rating = String.format("%.1f", movie.rating),
                        posterUrl = movie.posterUrl
                    )
                },
                topRatedMovies = homeScreenData.topRatedMovies.map { movie ->
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