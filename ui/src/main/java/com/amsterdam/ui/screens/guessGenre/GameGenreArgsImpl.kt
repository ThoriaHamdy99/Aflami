package com.amsterdam.ui.screens.guessGenre

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.amsterdam.viewmodel.game.whichGenre.GameGenreArgs

class GameGenreArgsImpl(savedStateHandle: SavedStateHandle): GameGenreArgs {
    override val difficulty = savedStateHandle.toRoute<GameGenreArgs>().difficulty
}