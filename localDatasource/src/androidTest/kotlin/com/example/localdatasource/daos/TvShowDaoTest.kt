package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.localdatasource.utils.createSearchMapping
import com.example.localdatasource.utils.createTvShow
import com.example.repository.dto.local.*
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var dao: TvShowDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.tvShowDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun addAllTvShows_shouldInsertTvShows() = runTest {
        //Arrange
        val tvShow = createTvShow()
        val localTvShowWithSearchDt =
            createSearchMapping(id = tvShow.tvShowId, keyword = "Breaking bad", language = "en")
        //Act
        dao.addAllTvShows(listOf(tvShow))
        dao.insertTvShowSearchMappings(listOf(localTvShowWithSearchDt))
        //Assert
        val results = dao.getTvShowsBySearchKeyword(
            localTvShowWithSearchDt.searchKeyword,
            SearchType.BY_KEYWORD,
            localTvShowWithSearchDt.storedLanguage,
            10,
            0
        )
        assertThat(results.first().tvShow).isEqualTo(tvShow)
    }
//
//    @Test
//    fun insertTvShowSearchMappings_shouldMakeTvShowQueryable() = runTest {
//        val tvShow = createTvShow(tvShowId = 10L)
//        val search = createSearchMapping(tvShow.tvShowId, "lost", "en")
//
//        dao.addAllTvShows(listOf(tvShow))
//        dao.insertTvShowSearchMappings(listOf(search))
//
//        val result = dao.getTvShowsBySearchKeyword("lost", SearchType.BY_KEYWORD, "en", 10, 0)
//        assertThat(result.map { it.tvShow }).containsExactly(tvShow)
//    }
//
//    @Test
//    fun getTvShowsBySearchKeyword_shouldRespectLimitAndOffset() = runTest {
//        val shows = (1L..5L).map { id -> createTvShow(id) }
//        val mappings = shows.map {
//            LocalTvShowWithSearchDto(
//                tvShowId = it.tvShowId,
//                searchKeyword = "top",
//                storedLanguage = it.storedLanguage
//            )
//        }
//
//        dao.addAllTvShows(shows)
//        dao.insertTvShowSearchMappings(mappings)
//
//        val result =
//            dao.getTvShowsBySearchKeyword("top", SearchType.BY_KEYWORD, "en", limit = 2, offset = 2)
//        assertThat(result).hasSize(2)
//        assertThat(result.map { it.tvShow.tvShowId }).containsExactly(3L, 4L)
//    }
}
