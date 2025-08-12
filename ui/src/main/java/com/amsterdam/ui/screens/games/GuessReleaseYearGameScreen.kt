package com.amsterdam.ui.screens.games

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.games.component.GameAppBar
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.guessReleseDateGame.GuessReleaseYearUiState


@Composable
fun GuessReleaseYearScreen() {

}

@Composable
private fun GuessReleaseYearContent(state: GuessReleaseYearUiState) {
    val pagerState = rememberPagerState { state.questionsCounts }
    Box {
        LoginBackground()
        HorizontalPager(pagerState) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    GameAppBar(
                        stringResource(R.string.when_is_released),
                        timerUiState = state.timerUiState
                    )
                }


            }
        }
    }
}