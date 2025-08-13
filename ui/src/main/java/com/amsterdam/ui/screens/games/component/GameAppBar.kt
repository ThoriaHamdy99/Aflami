package com.amsterdam.ui.screens.games.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.components.guessGame.TimerComponent
import com.amsterdam.viewmodel.sharedGame.TimerUiState

@Composable
fun GameAppBar(
    title: String, timerUiState: TimerUiState, modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AppTheme.color.surface, shape = RoundedCornerShape(12.dp))
        ) {
            Icon(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                title, tint = AppTheme.color.title
            )
            Text(
                title,
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title,
                modifier = Modifier.padding(start = 8.dp)
            )
            TimerComponent(timerUiState, modifier = Modifier.padding(start = 12.dp))
        }
    }
}