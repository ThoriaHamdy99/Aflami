package com.amsterdam.viewmodel.topRated

interface TopRatedInteractionListener {
    fun onClickMovie(movieId : Long)
    fun onClickBack()
    fun onClickRetryLoading()
}