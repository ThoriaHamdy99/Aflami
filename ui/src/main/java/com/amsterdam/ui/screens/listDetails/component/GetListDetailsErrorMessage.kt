package com.amsterdam.ui.screens.listDetails.component

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState.NoInternetError

fun getListDetailsErrorMessage(errorUiState: ErrorUiState?, context: Context): String{
    return when(errorUiState){
        NoInternetError  -> context.getString(R.string.offline_message)
        else -> ""
    }
}