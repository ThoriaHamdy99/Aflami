package com.amsterdam.viewmodel.shared.mappers

import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState

fun Actor.toUiState(): ActorUiState = ActorUiState(photo = imageUrl, name = name)
