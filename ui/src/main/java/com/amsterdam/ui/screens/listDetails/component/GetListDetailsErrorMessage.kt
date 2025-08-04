package com.amsterdam.ui.screens.listDetails.component

import android.content.Context
import android.util.Log
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.listDetails.ListDetailsError

fun getListDetailsErrorMessage(listDetailsError: ListDetailsError?, context: Context): String{
        Log.e("ListDetailsViewModel", "getListDetailsErrorMessage: $listDetailsError")
    return when(listDetailsError){
        ListDetailsError.NoNetwork -> context.getString(R.string.offline_message)
        else -> ""
    }
}