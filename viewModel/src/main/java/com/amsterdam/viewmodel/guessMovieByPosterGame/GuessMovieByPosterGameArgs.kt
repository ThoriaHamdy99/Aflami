package com.amsterdam.viewmodel.guessMovieByPosterGame

import androidx.lifecycle.SavedStateHandle

class GuessMovieByPosterGameArgs(savedStateHandle: SavedStateHandle) {

    val difficulty: String = savedStateHandle.get<String>(DIFFICULTY_ARGS)!!

    companion object {
        const val DIFFICULTY_ARGS = "difficulty"
    }
}