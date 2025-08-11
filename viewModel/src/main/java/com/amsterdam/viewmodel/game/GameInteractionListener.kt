package com.amsterdam.viewmodel.game

interface GameInteractionListener {
    fun onCancelGameClick()

    fun onChooseAnswerClick(answer: String)
}
