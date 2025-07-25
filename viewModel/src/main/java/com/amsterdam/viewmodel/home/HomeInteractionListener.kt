package com.amsterdam.viewmodel.home

import com.amsterdam.domain.models.Mood
import com.amsterdam.entity.category.MovieGenre

interface HomeInteractionListener {
    fun onClickRetryLoading()
    fun onClickSearch()
    fun onClickMovie(movieId : Long)
    fun onClickShowAllContinueWatchingMovies()
    fun onClickUpcomingMovieCard(id: Long)
    fun onChangeUpcomingMovieGenre(genre: MovieGenre)
    fun onClickShowAllToRatedMovies()

    fun onClickMood(mood: Mood)
    fun onClickGetNow()
    fun onDismissMoodPickerDialog()

    fun onClickViewDetails()
    fun onClickGetAnotherMovie()
}