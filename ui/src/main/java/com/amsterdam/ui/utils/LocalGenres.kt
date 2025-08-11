import com.amsterdam.entity.GenreUiModel
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.ui.R

object LocalGenres {
    val movieGenres = listOf(
        GenreUiModel(MovieGenre.SCIENCE_FICTION, R.string.science_fiction, R.drawable.img_sciencefiction),
        GenreUiModel(MovieGenre.FAMILY, R.string.family, R.drawable.img_family),
        GenreUiModel(MovieGenre.MYSTERY, R.string.mystery, R.drawable.img_mystery),
        GenreUiModel(MovieGenre.HISTORY, R.string.history, R.drawable.img_history),
        GenreUiModel(MovieGenre.WAR, R.string.war, R.drawable.img_war),
        GenreUiModel(MovieGenre.ACTION, R.string.action, R.drawable.img_action),
        GenreUiModel(MovieGenre.CRIME, R.string.crime, R.drawable.img_crime),
        GenreUiModel(MovieGenre.COMEDY, R.string.comedy, R.drawable.img_comedy),
        GenreUiModel(MovieGenre.HORROR, R.string.horror, R.drawable.img_horror),
        GenreUiModel(MovieGenre.WESTERN, R.string.western, R.drawable.img_western),
        GenreUiModel(MovieGenre.ROMANCE, R.string.romance, R.drawable.img_romance),
        GenreUiModel(MovieGenre.ADVENTURE, R.string.adventure, R.drawable.img_adventure),
        GenreUiModel(MovieGenre.TV_MOVIE, R.string.tv_movie, R.drawable.img_tvmovie),
        GenreUiModel(MovieGenre.FANTASY, R.string.fantasy, R.drawable.img_fantasy),
        GenreUiModel(MovieGenre.THRILLER, R.string.thriller, R.drawable.img_thriller),
        GenreUiModel(MovieGenre.DRAMA, R.string.drama, R.drawable.img_drama),
        GenreUiModel(MovieGenre.ANIMATION, R.string.animation, R.drawable.igm_animation),
        GenreUiModel(MovieGenre.MUSIC, R.string.music, R.drawable.img_music),
        GenreUiModel(MovieGenre.DOCUMENTARY, R.string.documentary, R.drawable.img_documentary)
    )

    val tvShowGenres = listOf(
        GenreUiModel(TvShowGenre.CRIME, R.string.crime, R.drawable.img_crime),
        GenreUiModel(TvShowGenre.FAMILY, R.string.family, R.drawable.img_family),
        GenreUiModel(TvShowGenre.MYSTERY, R.string.mystery, R.drawable.img_mystery),
        GenreUiModel(TvShowGenre.WAR_POLITICS, R.string.war, R.drawable.img_war),
        GenreUiModel(TvShowGenre.ANIMATION, R.string.animation, R.drawable.igm_animation),
        GenreUiModel(TvShowGenre.COMEDY, R.string.comedy, R.drawable.img_comedy),
        GenreUiModel(TvShowGenre.DRAMA, R.string.drama, R.drawable.img_drama),
        GenreUiModel(TvShowGenre.KIDS, R.string.kids, R.drawable.img_kids),
        GenreUiModel(TvShowGenre.ACTION_ADVENTURE, R.string.action_adventure, R.drawable.img_adventure),
        GenreUiModel(TvShowGenre.REALITY, R.string.reality, R.drawable.img_reality),
        GenreUiModel(TvShowGenre.SOAP, R.string.soap, R.drawable.img_soap),
        GenreUiModel(TvShowGenre.TALK, R.string.talk, R.drawable.img_talk),
        GenreUiModel(TvShowGenre.WESTERN, R.string.western, R.drawable.img_western),
        GenreUiModel(TvShowGenre.DOCUMENTARY, R.string.documentary, R.drawable.img_documentary)
    )
}
