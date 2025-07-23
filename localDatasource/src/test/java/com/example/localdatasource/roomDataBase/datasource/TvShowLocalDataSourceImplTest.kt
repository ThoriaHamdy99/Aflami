package com.example.localdatasource.roomDataBase.datasource

import com.example.entity.category.TvShowGenre
import com.example.localdatasource.roomDataBase.daos.TvShowCategoryInterestDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.localdatasource.utils.createTvShow
import com.example.repository.dto.local.LocalTvShowCategoryInterestDto
import com.example.repository.dto.local.LocalTvShowWithSearchDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowLocalDataSourceImplTest {

    private lateinit var tvShowDao: TvShowDao
    private lateinit var tvShowCategoryInterestDao: TvShowCategoryInterestDao
    private lateinit var dataSource: TvShowLocalDataSourceImpl

    @BeforeEach
    fun setUp() {
        tvShowDao = mockk(relaxed = true)
        tvShowCategoryInterestDao = mockk(relaxed = true)
        dataSource = TvShowLocalDataSourceImpl(tvShowDao, tvShowCategoryInterestDao)
    }

    @Test
    fun `getTvShowsByKeywordAndSearchType should return correct list`() = runTest {
        // Given
        val expected = listOf(mockk<TvShowWithCategory>())
        coEvery {
            tvShowDao.getTvShowsBySearchKeyword(
                keyword = "action",
                searchType = SearchType.BY_KEYWORD,
                storedLanguage = "en",
                limit = 10,
                offset = 0
            )
        } returns expected

        // When
        val result = dataSource.getTvShowsByKeywordAndSearchType(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            storedLanguage = "en",
            limit = 10,
            offset = 0
        )

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `addTvShows should insert shows and insert tv show search object`() = runTest {
        // Given
        val shows = listOf(
            createTvShow(id = 1, name = "Test Show", language = "en"),
        )
        val language = "en"
        val keyword = "test"

        // When
        dataSource.addTvShows(shows, language, keyword)

        // Then
        coVerify { tvShowDao.addAllTvShows(shows) }

        val expectedMappings = shows.map {
            LocalTvShowWithSearchDto(
                tvShowId = it.tvShowId,
                storedLanguage = language,
                searchKeyword = keyword
            )
        }

        coVerify { tvShowDao.insertTvShowSearchMappings(expectedMappings) }
    }

    @Test
    fun `incrementGenreInterest should call dao`() = runTest {
        // Given
        val genre = TvShowGenre.KIDS

        // When
        dataSource.incrementGenreInterest(genre)

        // Then
        coVerify { tvShowCategoryInterestDao.incrementInterest(genre) }
    }

    @Test
    fun `getAllGenreInterests should return map of genre to interest count`() = runTest {
        // Given
        val interestEntity = listOf(LocalTvShowCategoryInterestDto(TvShowGenre.KIDS, 3))
        coEvery { tvShowCategoryInterestDao.getAllInterests() } returns interestEntity

        // When
        val result = dataSource.getAllGenreInterests()

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                TvShowGenre.KIDS to 3
            )
        )
    }
}
