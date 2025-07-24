package com.example.viewmodel.home

import com.example.domain.models.Mood
import com.example.entity.category.MovieGenre
import com.example.viewmodel.shared.defaultMovieGenres
import com.example.viewmodel.shared.uiStates.MovieGenreItemUiState
import com.example.viewmodel.shared.uiStates.MovieItemUiState

data class HomeUiState(
    val popularMovies : List<PopularMovieItemUiState> = emptyList(),
    val upcomingMovies : List<MovieItemUiState> = emptyList(),
    val upcomingMovieGenres: List<MovieGenreItemUiState> = defaultMovieGenres,
    val topRatedMovies : List<MovieItemUiState> = emptyList(),
    val moodPickerUiState: MoodPickerUiState = MoodPickerUiState(),
    val isLoading : Boolean = false,
    val error : HomeError? = null
){
    data class PopularMovieItemUiState(
        val name : String = "",
        val rating: String = "" ,
        val posterUrl : String = ""
    )

    data class MoodPickerUiState(
        val moods: List<Mood> = listOf(
            Mood.SAD,
            Mood.THINKING,
            Mood.IN_LOVE,
            Mood.ANGRY,
            Mood.UN_HAPPY,
            Mood.CONFUSED
        ),
        val selectedMood: Mood? = null,
        val selectedMovie: MovieItemUiState = MovieItemUiState(),
        val movies: List<MovieItemUiState> = emptyList(),
        val isLoadingMovies: Boolean = false,
        val openMovieDialog: Boolean = false,
    )

    sealed class HomeError{
        data object NetworkError : HomeError()
    }

    fun getSelectedUpcomingMovieGenre(): MovieGenre {
        return upcomingMovieGenres
            .firstOrNull { it.selectableMovieGenre.isSelected }
            ?.selectableMovieGenre?.item ?: MovieGenre.ALL
    }
}
