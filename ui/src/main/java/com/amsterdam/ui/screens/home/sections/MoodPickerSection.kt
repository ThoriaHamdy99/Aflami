package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.domain.models.Mood
import com.amsterdam.ui.screens.home.component.MoodPickerCard
import com.amsterdam.ui.screens.home.model.CardMood
import com.amsterdam.viewmodel.home.HomeInteractionListener
import com.amsterdam.viewmodel.home.HomeUiState

@Composable
fun MoodPickerSection(
    state: HomeUiState,
    interactionListener: HomeInteractionListener
) {
    MoodPickerCard(
        cardMoods = state.moodPickerUiState.moods.map {
            CardMood.getModeByName(
                it.name
            )
        },
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
        onSelectMood = {
            val mood =
                Mood.getMoodByName(it.name)
            interactionListener.onClickMood(mood)
        },
        onClickGetNow = interactionListener::onClickGetNow
    )
}