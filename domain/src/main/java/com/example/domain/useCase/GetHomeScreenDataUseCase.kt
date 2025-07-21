package com.example.domain.useCase

import com.example.entity.Movie

class GetHomeScreenDataUseCase(
    private val getTopRatedMoviesUseCase : GetTopRatedMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getTopRatedMoviesUseCase.invoke(),
            popularMovies = getPopularMoviesUseCase.invoke()
        )
    }
    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val popularMovies: List<Movie>
    )
}