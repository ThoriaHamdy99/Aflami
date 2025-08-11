package com.amsterdam.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessPicture
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.viewmodel.game.GameInteractionListener
import com.amsterdam.viewmodel.game.GameQuestionUiState
import com.amsterdam.viewmodel.game.GameViewModel

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameScreenContent(
        state.questions,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun GameScreenContent(
    questions: List<GameQuestionUiState>,
    interactionListener: GameInteractionListener,
    modifier: Modifier = Modifier,
) {
    Box {
        LoginBackground()
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            val pagerState = rememberPagerState(pageCount = { questions.size })
            GameTopBar(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                PageIndicator(
                    pageCount = pagerState.pageCount,
                    currentPage = pagerState.currentPage,
                )
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
            ) { page ->
                GameQuestion(
                    question = questions[page],
                )
            }

            Box(modifier = Modifier.weight(1f))

            ConfirmButton(
                title = stringResource(R.string.next),
                onClick = {},
                isEnabled = true,
                isLoading = false,
                isNegative = false,
                modifier =
                    Modifier
                        .fillMaxWidth(),
            )
        }
    }
}

@Composable
fun GameQuestion(
    question: GameQuestionUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GuessPicture(
            blurRadius = 8.dp,
            points = 10,
            painter = painterResource(com.amsterdam.ui.R.drawable.bg_children_wearing_3d),
            isHintVisible = true,
            onClick = {},
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(question.answers) { answer ->
                AnswerSelectionItem(
                    text = answer,
                    status = AnswerStatus.Unselected,
                    onClick = {},
                )
            }
        }
    }
}

@Composable
fun GameTopBar(
    modifier: Modifier = Modifier,
    onCancelGameClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        title = {
            Text(
                text = "text",
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.large,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        leadingIcon = {
            IconButton(
                painter = painterResource(R.drawable.ic_cancel),
                tint = AppTheme.color.title,
                contentDescription = stringResource(R.string.back_to_menue),
                onClick = onCancelGameClick,
            )
        },
        trailingIcon = {
            // Timer
        },
    )
}

@ThemeAndLocalePreviews
@Composable
private fun GameScreenPreview() {
    AflamiTheme {
        GameScreen()
    }
}
