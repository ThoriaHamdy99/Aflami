package com.amsterdam.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amsterdam.designsystem.R
import com.amsterdam.ui.R as R2
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TopAppBar
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.entity.Game
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.PageIndicator
import com.amsterdam.ui.components.guessGame.GuessPicture
import com.amsterdam.ui.components.guessGame.GuessTitle
import com.amsterdam.ui.components.guessGame.TimerComponent
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.genre.uiModel
import com.amsterdam.viewmodel.game.whichGenre.GenreGameEffect
import com.amsterdam.viewmodel.game.whichGenre.GenreGameInteractionListener
import com.amsterdam.viewmodel.game.whichGenre.GameQuestionUiState
import com.amsterdam.viewmodel.game.whichGenre.GenreGameUiState
import com.amsterdam.viewmodel.game.whichGenre.GuessGenreViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GuessGenreViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    val context = LocalContext.current


    GameScreenContent(
        state = state,
        gameType = Game.GameType.GUESS_MOVIE_BY_GENRE,
        interactionListener = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun GameScreenContent(
    state: GenreGameUiState,
    gameType: Game.GameType,
    interactionListener: GenreGameInteractionListener,
    modifier: Modifier = Modifier,
) {
    Box {
        LoginBackground()
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(bottom = 16.dp)
                    .navigationBarsPadding(),
        ) {
            val pagerState = rememberPagerState(pageCount = { state.questions.size })
            val scope = rememberCoroutineScope()
            val topBarTitle =
                when (gameType) {
                    Game.GameType.GUESS_CHARACTER -> stringResource(R.string.guess_character_game_title)
                    Game.GameType.GUESS_MOVIE_BY_POSTER ->
                        stringResource(
                            com.amsterdam.ui.R.string.guess_movie_game_title,
                        )

                    Game.GameType.GUESS_MOVIE_BY_RELEASE -> stringResource(R.string.release_game_title)
                    Game.GameType.GUESS_MOVIE_BY_GENRE -> stringResource(R.string.genre_game_title)
                }

            LaunchedEffect(state.currentQuestionIndex) {
                scope.launch { pagerState.animateScrollToPage(state.currentQuestionIndex) }
            }
            GameTopBar(
                title = topBarTitle,
                timerUiState = state.timerUiState,
                onCancelGameClick = interactionListener::onCancelGameClick,
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
                contentPadding = PaddingValues(horizontal = 12.dp),
                pageSpacing = 12.dp,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
            ) { page ->
                GameQuestion(
                    question = state.questions[page],
                    gameType = gameType,
                    isHintEnabled = state.isHintEnabled,
                    selectedAnswerIndex = state.selectedAnswerIndex,
                    isAnswerCorrect = state.isAnswerCorrect,
                    onHintClick = interactionListener::onUseHint,
                    onSelectAnswer = { answerIndex, answer ->
                        interactionListener
                            .onChooseAnswerClick(
                                answer,
                                answerIndex,
                            )
                    },
                )
            }

            Box(modifier = Modifier.weight(1f))

            ConfirmButton(
                title = stringResource(R.string.next),
                onClick = interactionListener::onMoveToNextQuestion,
                isEnabled = state.isNextEnabled,
                isLoading = false,
                isNegative = false,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
fun GameQuestion(
    question: GameQuestionUiState,
    gameType: Game.GameType,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (answerIndex: Int, answer: MovieGenre) -> Unit = { _, _ -> },
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (gameType) {
            Game.GameType.GUESS_CHARACTER, Game.GameType.GUESS_MOVIE_BY_POSTER -> {
                var blurRadius by remember { mutableStateOf(8.dp) }
                GuessPicture(
                    blurRadius = blurRadius,
                    points = 10,
                    imageUrl = question.questionData,
                    isHintVisible = isHintEnabled,
                    onClick = {
                        blurRadius = 5.dp
                        onHintClick()
                    },
                )
            }

            Game.GameType.GUESS_MOVIE_BY_RELEASE, Game.GameType.GUESS_MOVIE_BY_GENRE -> {
                GuessTitle(
                    title = question.questionData,
                    points = 10,
                    isHintVisible = isHintEnabled,
                    onClick = onHintClick,
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(question.answers) { index, answer ->
                val state =
                    if (selectedAnswerIndex == index) {
                        when (isAnswerCorrect) {
                            true -> AnswerStatus.Correct
                            false -> AnswerStatus.Wrong
                            null -> AnswerStatus.Unselected
                        }
                    } else {
                        AnswerStatus.Unselected
                }

                AnswerSelectionItem(
                    text = stringResource(answer.uiModel.displayableName),
                    status = state,
                    onClick = {
                        if (isAnswerCorrect != null) return@AnswerSelectionItem
                        onSelectAnswer(index, answer)
                    },
                )
            }
        }
    }
}

@Composable
internal fun GameTopBar(
    title: String,
    timerUiState: TimerUiState,
    modifier: Modifier = Modifier,
    onCancelGameClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        containerColor = Color.Transparent,
        title = {
            Text(
                text = title,
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
            TimerComponent(timerUiState)
        },
    )
}

@ThemeAndLocalePreviews
@Composable
private fun GameScreenPreview() {
    AflamiTheme {
        GameScreenContent(
            state =
                GenreGameUiState(
                    questions = emptyList()
                ),
            gameType = Game.GameType.GUESS_MOVIE_BY_GENRE,
            interactionListener =
                object : GenreGameInteractionListener {
                    override fun onCancelGameClick() {}
                    override fun onChooseAnswerClick(
                        answer: MovieGenre,
                        answerIndex: Int
                    ) {}
                    override fun onUseHint() {}
                    override fun onMoveToNextQuestion() {}
                },
        )
    }
}
