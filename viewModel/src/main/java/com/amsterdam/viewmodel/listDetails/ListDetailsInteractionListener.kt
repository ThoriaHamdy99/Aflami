package com.amsterdam.viewmodel.listDetails

interface ListDetailsInteractionListener {
    fun onClickMovie(movieId: Long)
    fun onClickBack()
    fun onClickRetryLoading()
}