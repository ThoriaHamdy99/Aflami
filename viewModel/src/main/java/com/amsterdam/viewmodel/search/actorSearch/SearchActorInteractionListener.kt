package com.amsterdam.viewmodel.search.actorSearch

interface SearchActorInteractionListener {
    fun onUserSearchChange(query : String)
    fun onClickNavigateBack()
    fun onClickRetrySearch()
    fun onClickMovie(movieId : Long)
    fun onSaveSearchHistory()
}