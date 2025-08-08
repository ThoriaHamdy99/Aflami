package com.amsterdam.viewmodel.movieDetails

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.domain.useCase.list.AddMovieToListUseCase
import com.amsterdam.domain.useCase.list.CreateNewListUseCase
import com.amsterdam.domain.useCase.list.GetUserListsUseCase
import com.amsterdam.domain.useCase.myRating.movie.SetUserMovieRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private lateinit var viewModel: MovieDetailsViewModel
    private val getsSessionType: GetsSessionType = mockk(relaxed = true)
    private val testDispatcherProvider = TestDispatcherProvider()
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase = mockk(relaxed = true)
    private val addMovieToListUseCase: AddMovieToListUseCase = mockk(relaxed = true)
    private val getUserListsUseCase: GetUserListsUseCase = mockk(relaxed = true)
    private val createNewListUseCase: CreateNewListUseCase = mockk(relaxed = true)
    private val setUserMovieRatingUseCase: SetUserMovieRatingUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private var testArgs: MovieDetailsArgs = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        every { testArgs.movieId } returns 100L
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should trigger movie details load when language changes`() = runTest {
        // Given
        val movieId = 100L
        every { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(movieId) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = Movie(
                id = movieId,
                name = "Test Movie",
                description = "Description",
                posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1),
                categories = listOf(MovieGenre.ACTION),
                rating = 8.0f,
                popularity = 100.0,
                originCountry = "US",
                runTimeInMinutes = 120
            ),
            reviews = emptyList(),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
            productionCompanies = emptyList(),
            userRate = null
        )

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        coVerify(exactly = 2) { getMovieDetailsUseCase.invoke(movieId) }
    }

    @Test
    fun `init should update state with received movie id`() = runTest {
        // Given
        val movieId = 1L
        every { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.movieId).isEqualTo(movieId)
    }

    @Test
    fun `init should update state with movie details when success loaded the data`() = runTest {
        // Given
        val movieId = 99L
        every { testArgs.movieId } returns movieId
        val movieDetails = GetMovieDetailsUseCase.MovieDetails(
            movie = Movie(
                id = movieId,
                name = "Test Movie",
                description = "Description",
                posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1),
                categories = listOf(MovieGenre.ACTION),
                rating = 8.0f,
                popularity = 100.0,
                originCountry = "US",
                runTimeInMinutes = 120
            ),
            reviews = emptyList(),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
            productionCompanies = emptyList(),
            userRate = null
        )
        coEvery { getMovieDetailsUseCase.invoke(movieId) } returns movieDetails

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.movieId).isEqualTo(movieId)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `init should update error state and stop loading when failed load the data`() = runTest {
        // Given
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NoInternetException()

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.networkError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `init should handle NetworkException and stop loading`() = runTest {
        // Given
        coEvery { getMovieDetailsUseCase.invoke(any()) } throws NetworkException()

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.networkError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `init should not update the state when received unknown error`() = runTest {
        // Given
        val movieId = 100L
        every { testArgs.movieId } returns movieId
        coEvery { getMovieDetailsUseCase.invoke(movieId) } throws AflamiException()

        // When
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.movieId).isEqualTo(movieId)
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.networkError).isFalse()
    }

    @Test
    fun `onClickMovieExtras should update the state to be true for selected movie extras`() =
        runTest {
            // Given
            val selectedExtras = MovieExtras.REVIEWS
            viewModel = MovieDetailsViewModel(
                args = testArgs,
                getMovieDetailsUseCase = getMovieDetailsUseCase,
                addMovieToListUseCase = addMovieToListUseCase,
                getUserListsUseCase = getUserListsUseCase,
                createListUseCase = createNewListUseCase,
                getsSessionType = getsSessionType,
                setUserRatingUseCase = setUserMovieRatingUseCase,
                manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
                dispatcherProvider = testDispatcherProvider
            )
            advanceUntilIdle()

            // When
            viewModel.onClickMovieExtras(selectedExtras)
            advanceUntilIdle()

            // Then
            val selectedItem = viewModel.state.value.extraItem.find { it.item == selectedExtras }
            assertThat(selectedItem?.isSelected).isTrue()
            assertThat(viewModel.state.value.extraItem.filter { it.isSelected }).hasSize(1)
        }

    @Test
    fun `onClickShowAllCast should send NavigateToCastsScreenEffect`() = runTest {
        // Given
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }

        // When
        viewModel.onClickShowAllCast()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(MovieDetailsEffect.NavigateToCastsScreenEffect)
        collectJob.cancel()
    }

    @Test
    fun `onClickBack should send NavigateBackEffect`() = runTest {
        // Given
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }

        // When
        viewModel.onClickBack()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(MovieDetailsEffect.NavigateBackEffect)
        collectJob.cancel()
    }

    @Test
    fun `onRateClicked should show login dialog when user is a guest`() = runTest {
        // Given
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        coEvery { getMovieDetailsUseCase.invoke(any()) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = Movie(
                id = 100L,
                name = "Test Movie",
                description = "Description",
                posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1),
                categories = listOf(MovieGenre.ACTION),
                rating = 8.0f,
                popularity = 100.0,
                originCountry = "US",
                runTimeInMinutes = 120
            ),
            reviews = emptyList(),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
            productionCompanies = emptyList(),
            userRate = null
        )

        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onClickRate()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Test
    fun `onClickNavigateToLogin should send NavigateToLoginScreenEffect when its call`() = runTest {
        // Given
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }

        // When
        viewModel.onClickNavigateToLogin()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(MovieDetailsEffect.NavigateToLoginScreenEffect)
        assertThat(viewModel.state.value.isLoginDialogVisible).isFalse()
        collectJob.cancel()
    }

    @Test
    fun `onClickSimilarMovie should send NavigateToMovieDetailsEffect`() = runTest {
        // Given
        val similarMovieId = 200L
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }

        // When
        viewModel.onClickSimilarMovie(similarMovieId)
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(MovieDetailsEffect.NavigateToMovieDetails(similarMovieId))
        collectJob.cancel()
    }

    @Test
    fun `onDescriptionExpansionToggled should toggle isDescriptionExpanded state`() = runTest {
        // Given
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()
        assertThat(viewModel.state.value.isDescriptionExpanded).isFalse()

        // When
        viewModel.onDescriptionExpansionToggled()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isDescriptionExpanded).isTrue()
    }

    @Test
    fun `onReviewExpansionToggled should toggle isExpanded for specific review`() = runTest {
        // Given
        val reviewUsername = "user1"
        val reviews = listOf(
            Review(
                id = 1L,
                reviewerName = "Author 1",
                reviewerUsername = reviewUsername,
                rating = 8.5f,
                content = "This is a great movie!",
                date = java.time.LocalDate.of(2023, 1, 1).toKotlinLocalDate(),
                imageUrl = "url1"
            ),
            Review(
                id = 2L,
                reviewerName = "Author 2",
                reviewerUsername = "user2",
                rating = 7.0f,
                content = "It was okay.",
                date = java.time.LocalDate.of(2023, 2, 1).toKotlinLocalDate(),
                imageUrl = "url2"
            )
        )
        coEvery { getMovieDetailsUseCase.invoke(any()) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = Movie(
                id = 100L,
                name = "Test Movie",
                description = "Description",
                posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1),
                categories = listOf(MovieGenre.ACTION),
                rating = 8.0f,
                popularity = 100.0,
                originCountry = "US",
                runTimeInMinutes = 120
            ),
            reviews = reviews,
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
            productionCompanies = emptyList(),
            userRate = null
        )
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onReviewExpansionToggled(reviewUsername)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.reviews.find { it.username == reviewUsername }?.isExpanded).isTrue()
    }

    @Test
    fun `onClickAddToList should show add to list dialog when user is logged in`() = runTest {
        // Given
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { getUserListsUseCase.invoke() } returns emptyList()
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onClickAddToList()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isAddToListDialogVisible).isTrue()
    }

    @Test
    fun `onClickAddToList should show login dialog when user is a guest`() = runTest {
        // Given
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onClickAddToList()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Test
    fun `onSaveMovieToList should call addMovieToListUseCase and send success effect`() = runTest {
        // Given
        val movieId = 100L
        val listId = 1L
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }
        coEvery { addMovieToListUseCase.invoke(listId, movieId.toInt()) } returns Unit

        // When
        viewModel.onSaveMovieToList(movieId.toInt(), listId)
        advanceUntilIdle()

        // Then
        coVerify { addMovieToListUseCase.invoke(listId, movieId.toInt()) }
        assertThat(effects).contains(MovieDetailsEffect.MovieAddedToListSuccessfully)
        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
        collectJob.cancel()
    }

    @Test
    fun `onClickCreateList should show create new list dialog`() = runTest {
        // Given
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onClickCreateList()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isCreateNewListDialogVisible).isTrue()
        assertThat(viewModel.state.value.isAddToListDialogVisible).isFalse()
    }

    @Test
    fun `onChangeListName should update listName in state`() = runTest {
        // Given
        val newListName = "My New List"
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onChangeListName(newListName)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.listName).isEqualTo(newListName)
    }

    @Test
    fun `onClickCreateNewList should call createListUseCase and add movie to the new list`() = runTest {
        // Given
        val newListName = "New List"
        val newListId = 5
        val movieId = 100L
        every { testArgs.movieId } returns movieId
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }
        coEvery { createNewListUseCase.invoke(newListName) } returns newListId
        coEvery { addMovieToListUseCase.invoke(newListId.toLong(), movieId.toInt()) } returns Unit

        // When
        viewModel.onChangeListName(newListName)
        viewModel.onClickCreateNewList()
        advanceUntilIdle()

        // Then
        coVerify { createNewListUseCase.invoke(newListName) }
        coVerify { addMovieToListUseCase.invoke(newListId.toLong(), movieId.toInt()) }
        assertThat(effects).contains(MovieDetailsEffect.ListCreatedSuccessfully)
        assertThat(effects).contains(MovieDetailsEffect.MovieAddedToListSuccessfully)
        assertThat(viewModel.state.value.isCreateNewListDialogVisible).isFalse()
        collectJob.cancel()
    }

    @Test
    fun `onClickCreateNewList should send FailedToCreateList effect on error`() = runTest {
        // Given
        val newListName = "New List"
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }
        coEvery { createNewListUseCase.invoke(newListName) } throws AflamiException()

        // When
        viewModel.onChangeListName(newListName)
        viewModel.onClickCreateNewList()
        advanceUntilIdle()

        // Then
        coVerify { createNewListUseCase.invoke(newListName) }
        assertThat(effects).contains(MovieDetailsEffect.FailedToCreateList)
        collectJob.cancel()
    }

    @Test
    fun `onSelectedListChange should update selectedList in state`() = runTest {
        // Given
        val selectedList = UserListUiState(id = 1L, name = "Test List")
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        advanceUntilIdle()

        // When
        viewModel.onSelectedListChange(selectedList)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.selectedList).isEqualTo(selectedList)
    }

    @Test
    fun `onClickSubmit should call setUserRatingUseCase and send success effect`() = runTest {
        // Given
        val movieId = 100L
        val rating = 4
        every { testArgs.movieId } returns movieId
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        val effects = mutableListOf<MovieDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it!!) } }
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { setUserMovieRatingUseCase.setUserMovieRate(rating, movieId) } returns Unit

        // When
        viewModel.onClickRate()
        viewModel.onChangeRating(rating)
        viewModel.onClickSubmit()
        advanceUntilIdle()

        // Then
        coVerify { setUserMovieRatingUseCase.setUserMovieRate(rating, movieId) }
        assertThat(effects).contains(MovieDetailsEffect.ShowRatingSuccessSnackBar)
        assertThat(viewModel.state.value.rateDialogUiState.isVisible).isFalse()
        collectJob.cancel()
    }

    @Test
    fun `onChangeRating should update selected star and enable submit button when rate changes`() = runTest {
        // Given
        val movieId = 100L
        val previousRating = 0
        every { testArgs.movieId } returns movieId
        viewModel = MovieDetailsViewModel(
            args = testArgs,
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            addMovieToListUseCase = addMovieToListUseCase,
            getUserListsUseCase = getUserListsUseCase,
            createListUseCase = createNewListUseCase,
            getsSessionType = getsSessionType,
            setUserRatingUseCase = setUserMovieRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { getMovieDetailsUseCase.invoke(any()) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = Movie(
                id = 100L,
                name = "Test Movie",
                description = "Description",
                posterUrl = "",
                releaseDate = LocalDate(2023, 1, 1),
                categories = listOf(MovieGenre.ACTION),
                rating = 8.0f,
                popularity = 100.0,
                originCountry = "US",
                runTimeInMinutes = 120
            ),
            reviews = emptyList(),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
            productionCompanies = emptyList(),
            userRate = previousRating
        )

        viewModel.onClickRate()
        advanceUntilIdle()
        assertThat(viewModel.state.value.rateDialogUiState.isSubmittingEnabled).isFalse()

        // When
        viewModel.onChangeRating(4)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.rateDialogUiState.selectedStarIndex).isEqualTo(4)
        assertThat(viewModel.state.value.rateDialogUiState.isSubmittingEnabled).isTrue()
    }

}