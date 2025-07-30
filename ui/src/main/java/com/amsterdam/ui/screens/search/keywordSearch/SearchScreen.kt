package com.amsterdam.ui.screens.search.keywordSearch

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.components.NoDataContainer
import com.amsterdam.ui.components.NoNetworkContainer
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.navigation.Route.MovieDetails
import com.amsterdam.ui.navigation.Route.SeriesDetails
import com.amsterdam.ui.screens.search.keywordSearch.sections.filterDialog.FilterDialog
import com.amsterdam.ui.screens.search.keywordSearch.sections.recentSearchesSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.searchScreenHeaderSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.successMediaItemsSection
import com.amsterdam.ui.screens.search.keywordSearch.sections.suggestionsHubSection
import com.amsterdam.ui.utils.safeNavigate
import com.amsterdam.viewmodel.search.keywordSearch.FilterInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchErrorState
import com.amsterdam.viewmodel.search.keywordSearch.SearchInteractionListener
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiEffect
import com.amsterdam.viewmodel.search.keywordSearch.SearchUiState
import com.amsterdam.viewmodel.search.keywordSearch.SearchViewModel
import com.amsterdam.viewmodel.search.keywordSearch.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.shared.uiStates.TvShowItemUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movieFlow = state.movies.collectAsLazyPagingItems()
    val tvShowFlow = state.tvShows.collectAsLazyPagingItems()
    val navController = LocalNavController.current
    LaunchedEffect(movieFlow.loadState) {
        if (state.selectedTabOption == TabOption.MOVIES) {
            viewModel.onPagingLoadStateChanged(movieFlow.loadState)
        }
    }
    LaunchedEffect(tvShowFlow.loadState) {
        if (state.selectedTabOption == TabOption.TV_SHOWS) {
            viewModel.onPagingLoadStateChanged(tvShowFlow.loadState)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            effect?.let {
                when (effect) {
                    SearchUiEffect.NavigateBack -> navController.popBackStack()
                    SearchUiEffect.NavigateToActorSearch -> navController.safeNavigate(Route.SearchByActor)
                    SearchUiEffect.NavigateToWorldSearch -> navController.safeNavigate(Route.SearchByCountry)
                    is SearchUiEffect.NavigateToMovieDetails -> {
                        viewModel.onSaveSearchHistory()
                        navController.safeNavigate(MovieDetails(effect.movieId))
                    }

                    is SearchUiEffect.NavigateToTvShowDetails -> {
                        viewModel.onSaveSearchHistory()
                        navController.navigate(SeriesDetails(effect.tvShowId))
                    }
                }
            }
        }
    }

    SearchContent(
        state = state,
        movies = movieFlow,
        tvShows = tvShowFlow,
        interaction = viewModel,
        filterInteraction = viewModel,
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun SearchContent(
    state: SearchUiState,
    movies: LazyPagingItems<MovieItemUiState>,
    tvShows: LazyPagingItems<TvShowItemUiState>,
    interaction: SearchInteractionListener,
    filterInteraction: FilterInteractionListener,
) {
    BackHandler(enabled = state.keyword.isNotEmpty()) {
        interaction.onClickClearSearch()
    }
    var headerHeight by remember { mutableStateOf(0.dp) }

    var isPageStillLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        columns = GridCells.Adaptive(160.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {

        searchScreenHeaderSection(
            keyword = state.keyword,
            selectedTabOption = state.selectedTabOption,
            onNavigateBackClicked = interaction::onClickNavigateBack,
            onKeywordValuedChanged = interaction::onChangeSearchKeyword,
            onFilterButtonClicked = interaction::onClickFilterButton,
            onTabOptionClicked = interaction::onClickTabOption,
            onSaveSearchHistory = interaction::onSaveSearchHistory,
            onHeaderSizeChanged = { headerHeight = it.height.dp },
            keyboardController = keyboardController
        )

        successMediaItemsSection(
            selectedTabOption = state.selectedTabOption,
            moviesFlow = movies,
            tvShowsFlow = tvShows,
            onPageLoading = { isPageStillLoading = it },
            onMovieClicked = interaction::onClickMovieCard,
            onTvShowClicked = interaction::onClickTvShowCard,
            state = state
        )

        suggestionsHubSection(
            keyword = state.keyword,
            onWorldSearchCardClicked = interaction::onClickWorldSearchCard,
            onActorSearchCardClicked = interaction::onClickActorSearchCard
        )

        recentSearchesSection(
            keyword = state.keyword,
            recentSearches = state.recentSearches,
            onAllRecentSearchesCleared = interaction::onClickClearAllRecentSearches,
            onRecentSearchClicked = interaction::onClickRecentSearch,
            onRecentSearchCleared = interaction::onClickClearRecentSearch,
            headerHeight = headerHeight
        )

        item(span = { GridItemSpan(maxLineSpan) }) {
            AnimatedVisibility(modifier = Modifier.fillMaxSize(), visible = state.isDialogVisible) {
                FilterDialog(
                    filterState = state.filterItemUiState,
                    selectedTabOption = state.selectedTabOption,
                    interaction = filterInteraction
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            AnimatedVisibility(
                visible = (state.isLoading || isPageStillLoading) && state.keyword.isNotBlank() && state.errorUiState == null,
            ) {
                CenterOfScreenContainer(unneededSpace = headerHeight) {
                    LoadingContainer()
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {

            AnimatedVisibility(state.keyword.isNotBlank()) {
                if (state.errorUiState != null && state.errorUiState is SearchErrorState.NoNetworkConnection) {
                    NoNetworkContainer(onClickRetry = interaction::onClickRetryRequest)
                }

                val isSelectedTabSearchResultEmpty =
                    if (state.selectedTabOption == TabOption.MOVIES) {
                        movies.itemSnapshotList.isEmpty()
                    } else {
                        tvShows.itemSnapshotList.isEmpty()
                    }

                AnimatedVisibility(!state.isLoading && state.errorUiState == null && isSelectedTabSearchResultEmpty) {
                    NoDataContainer(
                        imageRes = painterResource(com.amsterdam.ui.R.drawable.placeholder_no_result_found),
                        title = stringResource(R.string.no_search_result),
                        description = stringResource(R.string.no_search_result_description),
                    )
                }
            }
        }
    }
}