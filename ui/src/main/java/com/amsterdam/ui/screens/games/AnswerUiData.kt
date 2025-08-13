package com.amsterdam.ui.screens.games

import com.amsterdam.ui.components.selection.AnswerStatus
import com.amsterdam.viewmodel.sharedGame.AnswerStatusUiState

fun AnswerStatusUiState.toAnswerStatus(): AnswerStatus {
    return when (this) {
        AnswerStatusUiState.Correct -> AnswerStatus.Correct
        AnswerStatusUiState.Unselected -> AnswerStatus.Unselected
        AnswerStatusUiState.Wrong -> AnswerStatus.Wrong
    }
}