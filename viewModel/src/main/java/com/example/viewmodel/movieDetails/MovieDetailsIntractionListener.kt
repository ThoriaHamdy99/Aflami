package com.example.viewmodel.movieDetails

import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras

interface MovieDetailsInteractionListener  {
    fun onClickMovieExtras(movieExtras: MovieExtras)
    fun onClickShowAllCast()
    fun onClickBack()
    fun onClickRetryRequest()
    fun onLastOptionClicked(title: String)
    fun onFirstOptionClicked(title: String)
    fun onLoginClicked()
    fun onCancelClicked()
}