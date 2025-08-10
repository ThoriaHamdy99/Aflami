package com.amsterdam.domain.models.gameModels

sealed class HintState {
    object None : HintState()
    object BlurReduced : HintState()
    data class OptionRemoved(val removedOptionIds: Set<String>) : HintState()
}