package com.amsterdam.ui.screens.movieDetails.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.viewmodel.shared.movieAndSeriseDetails.ActorUiState

@Composable
fun CastSection(
    modifier: Modifier = Modifier,
    actors: List<ActorUiState>,
    onClickAllCast: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.cast),
                style = AppTheme.textStyle.headline.small,
                color = AppTheme.color.title,
            )
            Text(
                text = stringResource(R.string.all),
                style = AppTheme.textStyle.label.medium,
                color = AppTheme.color.primary,
                modifier = Modifier.clickable(onClick = onClickAllCast)
            )
        }
        LazyRow(
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(actors) {
                ActorCard(actor = it)
            }
        }
    }
}