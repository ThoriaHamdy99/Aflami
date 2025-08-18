package com.amsterdam.ui.screens.profile.components

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState

fun getProfileErrorMessage(profileErrorState: ErrorUiState?, context: Context): String{
    return when(profileErrorState){
        ErrorUiState.UnknownError -> context.getString(R.string.search_error_unknown)
        else -> ""
    }
}