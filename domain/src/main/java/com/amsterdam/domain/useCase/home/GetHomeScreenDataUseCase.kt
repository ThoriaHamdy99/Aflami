package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.model.category.MovieGenre
import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow

class GetHomeScreenDataUseCase(
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
) {

    suspend operator fun invoke(): HomeScreenData {
        return HomeScreenData(
            topRatedMovies = getTopRatedMoviesUseCase(),
            topRatedTvShows = getTopRatedTvShowsUseCase(),
            popularMovies = getPopularMoviesUseCase(),
            popularTvShows = getPopularTvShowsUseCase(),
            upComingMovies = getUpcomingMoviesUseCase(MovieGenre.ALL)
        )
    }

    data class HomeScreenData(
        val topRatedMovies: List<Movie>,
        val topRatedTvShows: List<TvShow>,
        val popularMovies: List<Movie>,
        val popularTvShows: List<TvShow>,
        val upComingMovies: List<Movie>
    )
}