package com.example.ui.screens.movieDetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.viewmodel.shared.movieAndSeriseDetails.MovieAndSeriesDetailsDialogType

@Composable
fun MovieAndSeriesDetailsDialogType?.getMovieAndSeriesDetailsDialogTitle(): String {
    return when(this){
        MovieAndSeriesDetailsDialogType.AddToList -> stringResource(R.string.add_to_list)
        MovieAndSeriesDetailsDialogType.Rate -> stringResource(R.string.rate)
        null -> ""
    }
}