package com.amsterdam.viewmodel.letsPlay

sealed interface LetsPlayEffect {
    data class NavigateToGameScreen(val difficulty: String) : LetsPlayEffect
}
