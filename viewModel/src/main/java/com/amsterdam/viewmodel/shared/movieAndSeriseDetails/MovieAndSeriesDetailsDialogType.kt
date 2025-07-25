package com.amsterdam.viewmodel.shared.movieAndSeriseDetails

sealed interface MovieAndSeriesDetailsDialogType {
    data object Rate : MovieAndSeriesDetailsDialogType
    data object AddToList : MovieAndSeriesDetailsDialogType
}