package com.amsterdam.viewmodel.sharedGame

import java.io.Serializable

data class GameResultUiState(
    val totalCollectedPoints: Int = 0,
    val totalSpentSeconds: Int = 0
) : Serializable