package com.amsterdam.ui.screens.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.R
import com.amsterdam.ui.components.guessGame.GuessTitle
import com.amsterdam.ui.screens.games.component.GameAppBar
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.ui.screens.onBoarding.component.PageIndicator
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
                item {
                    PageIndicator(
                        pageCount = state.questionsCounts,
                        currentPage = state.currentQuestionIndex + 1,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

            }
        }
    }
}