package com.example.viewmodel.home

interface HomeInteractionListener {
    fun onClickRetryLoading()
    fun onClickSearch()
    fun onClickMovie(movieId : Long)
    fun onClickShowAllToRatedMovies()
}