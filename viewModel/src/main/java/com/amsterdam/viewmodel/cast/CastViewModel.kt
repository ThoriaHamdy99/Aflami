package com.amsterdam.viewmodel.cast

import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CastViewModel @Inject constructor(
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getTvShowCastUseCase: GetTvShowCastUseCase,
    private val args: CastScreenArgs,
    dispatcherProvider: DispatcherProvider
) : BaseViewModel<CastUiState, CastUiEffect>(CastUiState(), dispatcherProvider),
    CastInteractionListener {

    init {
        getCast()
    }

    private fun getCast() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::getCastList,
            onSuccess = ::onGetCastSuccess,
            onCompletion = ::onGetCastCompletion,
        )
    }

    private suspend fun getCastList(): List<Actor> {
        return when(MediaType.valueOf(args.mediaType)) {
            MediaType.MOVIE -> getMovieCastUseCase(args.mediaId)
            MediaType.TV_SHOW -> getTvShowCastUseCase(args.mediaId)
        }
    }

    private fun onGetCastSuccess(cast: List<Actor>) {
        updateState { it.copy(cast = cast.toActorsUiState()) }
        resetErrorStateToNull()
    }

    private fun onGetCastCompletion() = updateState { it.copy(isLoading = false) }

    override fun onClickNavigateBack() = sendNewNavigationEffect(CastUiEffect.NavigateBack)

    override fun onClickRetrySearch() = getCast()
}