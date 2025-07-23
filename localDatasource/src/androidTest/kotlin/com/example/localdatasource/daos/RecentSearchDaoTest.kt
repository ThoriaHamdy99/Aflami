package com.example.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.utils.createLocalSearchDto
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchDaoTest {

    private lateinit var database: AflamiDatabase
    private lateinit var dao: RecentSearchDao

    @BeforeEach
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
        dao = database.recentSearchDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }


    @Test
    fun upsertRecentSearch_shouldInsertNewEntry() = runTest {
        // Given
        val search = createLocalSearchDto("barbie", SearchType.BY_KEYWORD)

        // Act
        dao.upsertRecentSearch(search)

        // Then
        val result = dao.getSearchByKeywordAndType("barbie", SearchType.BY_KEYWORD, "en")
        assertThat(result!!.toComparable()).isEqualTo(search.toComparable())
    }

    @Test
    fun upsertRecentSearch_shouldUpdateExistingEntry() = runTest {
        // Given
        val original = createLocalSearchDto("oppenheimer", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 1000)
        val updated = createLocalSearchDto("oppenheimer", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 999_999)

        // Act
        dao.upsertRecentSearch(original)
        dao.upsertRecentSearch(updated)

        // Then
        val result = dao.getSearchByKeywordAndType("oppenheimer", SearchType.BY_KEYWORD, "en")
        assertThat(result!!.toComparable()).isEqualTo(updated.toComparable())
    }


    @Test
    fun getRecentSearches_shouldReturnOrderedByExpireDesc_andGroupedByKeyword() = runTest {
        // Given
        val search1 = createLocalSearchDto("a", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 500)
        val search2 = createLocalSearchDto("b", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 800)
        val search3 = createLocalSearchDto("a", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 1000)

        dao.upsertRecentSearch(search1)
        dao.upsertRecentSearch(search2)
        dao.upsertRecentSearch(search3)

        // Act
        val result = dao.getRecentSearches(SearchType.BY_KEYWORD)

        // Then
        val expected = listOf(search3, search2).map { it.toComparable() }
        assertThat(result.map { it.toComparable() }).containsExactlyElementsIn(expected)
    }


    @Test
    fun deleteAllSearches_shouldRemoveAll() = runTest {
        // Given
        dao.upsertRecentSearch(createLocalSearchDto("foo", SearchType.BY_KEYWORD))
        dao.upsertRecentSearch(createLocalSearchDto("bar", SearchType.BY_KEYWORD))
        //Act
        dao.deleteAllSearches()
        //Then
        val result = dao.getRecentSearches(SearchType.BY_KEYWORD)
        assertThat(result).isEmpty()
    }

    @Test
    fun deleteAllExpiredSearches_shouldRemoveOnlyExpiredItems() = runTest {
        // Given
        val expired = createLocalSearchDto("old", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = -10_000)
        val fresh = createLocalSearchDto("new", SearchType.BY_KEYWORD, expireOffsetInMilliseconds = 10_000)

        dao.upsertRecentSearch(expired)
        dao.upsertRecentSearch(fresh)
        //Act
        dao.deleteAllExpiredSearches(Clock.System.now())
        //Then
        val result = dao.getRecentSearches(SearchType.BY_KEYWORD)

        assertThat(result.map { it.toComparable() }).containsExactly(fresh.toComparable())
    }

    @Test
    fun deleteSearchByKeyword_shouldRemoveCorrectSearch() = runTest {
        // Given
        val search1 = createLocalSearchDto("target", SearchType.BY_KEYWORD)
        val search2 = createLocalSearchDto("keep", SearchType.BY_KEYWORD)

        dao.upsertRecentSearch(search1)
        dao.upsertRecentSearch(search2)
        //Act
        dao.deleteSearchByKeyword("target", SearchType.BY_KEYWORD, "en")
        //Then
        val result = dao.getRecentSearches(SearchType.BY_KEYWORD)
        assertThat(result.map { it.toComparable() }).containsExactly(search2.toComparable())
    }

    @Test
    fun getSearchByKeywordAndType_shouldReturnCorrectSearch() = runTest {
        // Given
        val search = createLocalSearchDto("unique", SearchType.BY_KEYWORD)
        dao.upsertRecentSearch(search)
        //Act
        val result = dao.getSearchByKeywordAndType("unique", SearchType.BY_KEYWORD, "en")
        //Then
        assertThat(result!!.toComparable()).isEqualTo(search.toComparable())
    }

    data class ComparableSearchDto(
        val searchKeyword: String,
        val searchType: SearchType,
        val storedLanguage: String,
        val expireDateMillis: Long
    )

    fun LocalSearchDto.toComparable(): ComparableSearchDto {
        return ComparableSearchDto(
            searchKeyword = this.searchKeyword,
            searchType = this.searchType,
            storedLanguage = this.storedLanguage,
            expireDateMillis = this.expireDate.toEpochMilliseconds()
        )
    }

}
