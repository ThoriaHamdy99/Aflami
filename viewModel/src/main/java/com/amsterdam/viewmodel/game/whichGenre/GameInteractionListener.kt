package com.amsterdam.viewmodel.game.whichGenre

interface GameInteractionListener {
    fun onCancelGameClick()

    fun onChooseAnswerClick(answerIndex: Int, answer: String, questionId: Long)

    fun onUseHint()

    fun onMoveToNextQuestion()
}
