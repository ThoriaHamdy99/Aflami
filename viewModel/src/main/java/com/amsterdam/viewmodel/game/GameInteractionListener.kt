package com.amsterdam.viewmodel.game

interface GameInteractionListener {
    fun onCancelGameClick()

    fun onChooseAnswerClick(answerIndex: Int, answer: String, questionId: Long)

    fun onUseHint()

    fun onMoveToNextQuestion()
}
