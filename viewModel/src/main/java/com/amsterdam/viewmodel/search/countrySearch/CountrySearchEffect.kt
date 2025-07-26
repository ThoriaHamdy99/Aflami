package com.amsterdam.viewmodel.search.countrySearch

sealed interface CountrySearchEffect {
    data object NavigateBack : CountrySearchEffect
    object NavigateToMovieDetails : CountrySearchEffect
}