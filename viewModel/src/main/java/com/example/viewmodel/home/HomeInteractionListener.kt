package com.example.viewmodel.home

import com.example.domain.models.Mood
import com.example.entity.category.MovieGenre

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