package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchLocalDataSourceImplTest {
    private lateinit var dao: RecentSearchDao
    private lateinit var recentSearchLocalDataSourceImpl: RecentSearchLocalDataSourceImpl

    @BeforeEach
    fun setUp(){
        dao = mockk(relaxed = true)
        recentSearchLocalDataSourceImpl = RecentSearchLocalDataSourceImpl(dao)
    }

    @Test
    fun `upsertRecentSearch should call dao with correct data`()=runTest{
        //Given
        val recentSearch = LocalSearchDto(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            expireDate =Instant.fromEpochMilliseconds(2000),
            storedLanguage = "en"
        )
        //When
       recentSearchLocalDataSourceImpl.upsertRecentSearch(recentSearch)
        //Then
        coVerify (exactly = 1) { dao.upsertRecentSearch(recentSearch) }

    }
    @Test
    fun `getRecentSearches should return list of LocalSearchDto`()=runTest {
        //Given
        val expected = listOf(
            LocalSearchDto(
                searchKeyword = "action",
                searchType = SearchType.BY_KEYWORD,
                expireDate = Instant.fromEpochMilliseconds(2000),
                storedLanguage = "en"
            )
        )
        coEvery { dao.getRecentSearches(SearchType.BY_KEYWORD) } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getRecentSearches(SearchType.BY_KEYWORD)
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getRecentSearches should return empty list when dao return empty list`()=runTest {
        coEvery { dao.getRecentSearches(SearchType.BY_KEYWORD) } returns emptyList()
        //When
        val result = recentSearchLocalDataSourceImpl.getRecentSearches(SearchType.BY_KEYWORD)
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getSearchByKeywordAndType should return local search dto when its exists in the dao`()=runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        val expected = LocalSearchDto(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            expireDate = Instant.fromEpochMilliseconds(2000),
            storedLanguage = "en"
        )
        coEvery { dao.getSearchByKeywordAndType(keyword,searchType,"en") } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getSearchByKeywordAndType(keyword, searchType,"en")
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSearchByKeywordAndType should return null when not found the search keyword when not found the search key word`()=runTest {
        //Given
        val keyword = "action"
        val searchType = SearchType.BY_KEYWORD
        val expected = LocalSearchDto(
            searchKeyword = "action",
            searchType = SearchType.BY_KEYWORD,
            expireDate = Instant.fromEpochMilliseconds(2000),
            storedLanguage = "en"
        )
        coEvery { dao.getSearchByKeywordAndType(keyword,searchType,"en") } returns expected
        //When
        val result = recentSearchLocalDataSourceImpl.getSearchByKeywordAndType(keyword, searchType,"en")
        //Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `deleteRecentSearches should call delete function in the dao`()=runTest {
        //When
        recentSearchLocalDataSourceImpl.deleteRecentSearches()
        //Then
        coVerify { dao.deleteAllSearches() }
    }

    @Test
    fun `deleteRecentSearchByKeywordAndType should delete search entry and its cross-referenced movies`() = runTest {
        // Given
        val keyword = "action"
        val language = "en"
        val type = SearchType.BY_KEYWORD

        // When
        recentSearchLocalDataSourceImpl.deleteRecentSearchByKeywordAndType(keyword, type, language)

        // Then
        coVerify { dao.deleteSearchByKeyword(keyword, type, language) }
        coVerify { dao.deleteSearchMovieCrossRefByKeyword(keyword, type, language) }
    }

    @Test
    fun `deleteExpiredRecentSearches should call deleteAllExpiredSearches with provided date in the da`()=runTest {
        //Given
        val date = Instant.fromEpochMilliseconds(2000)
        //When
        recentSearchLocalDataSourceImpl.deleteExpiredRecentSearches(date)
        //Then
        coVerify { dao.deleteAllExpiredSearches(date) }
    }
}
