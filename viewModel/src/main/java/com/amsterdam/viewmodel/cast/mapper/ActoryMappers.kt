package com.amsterdam.viewmodel.cast.mapper

import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.cast.CastUiState.CastItemUiState

fun Actor.toUiState() = CastItemUiState(
    actorImage = imageUrl,
    actorName = name,
)