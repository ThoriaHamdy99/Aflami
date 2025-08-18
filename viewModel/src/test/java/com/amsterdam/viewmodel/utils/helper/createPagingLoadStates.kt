package com.amsterdam.viewmodel.utils.helper

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

fun createPagingLoadStates(
    state: LoadState
) = CombinedLoadStates(
    refresh = state,
    prepend = state,
    append = state,
    source = LoadStates(state, state, state),
    mediator = LoadStates(state, state, state),
)