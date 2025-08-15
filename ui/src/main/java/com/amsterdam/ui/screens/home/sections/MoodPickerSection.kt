package com.amsterdam.ui.screens.home.sections

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.domain.utils.Mood
import com.amsterdam.ui.screens.home.component.MoodPickerCard
import com.amsterdam.ui.screens.home.model.CardMood
import com.amsterdam.viewmodel.home.HomeInteractionListener
import com.amsterdam.viewmodel.home.HomeUiState

@Composable
fun MoodPickerSection(
    state: HomeUiState.MoodPickerUiState,
    interactionListener: HomeInteractionListener,
    modifier: Modifier=Modifier
) {
    MoodPickerCard(
        cardMoods = state.moods.map {
            CardMood.getModeByName(
                it.name
            )
        },
        selectedMood = state.selectedMood?.let { CardMood.getModeByName(it.name) },
        isButtonEnabled = state.selectedMood != null,
        isLoading = state.isLoadingMovies,
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 26.dp),
        onSelectMood = {
            val mood =
                Mood.getMoodByName(it.name)
            interactionListener.onChangeMood(mood)
        },
        onClickGetNow = interactionListener::onClickGetNow
    )
}