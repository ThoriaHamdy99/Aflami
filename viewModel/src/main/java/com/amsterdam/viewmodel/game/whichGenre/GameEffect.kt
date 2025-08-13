package com.amsterdam.viewmodel.game.whichGenre

sealed interface GameEffect {
    object CancelGame : GameEffect

    object GameOver : GameEffect
}
