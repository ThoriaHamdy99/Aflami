package com.amsterdam.localdatasource.daos

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.repository.dto.local.LocalSearchDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RecentSearchDaoTest {
    private lateinit var dao: RecentSearchDao
    private val context by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val database by lazy {
        Room.inMemoryDatabaseBuilder(context, AflamiDatabase::class.java).build()
    }

    @BeforeEach
    fun setup() {
        dao = database.recentSearchDao()
    }

    @AfterEach
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertRecentSearch_shouldInsertNewEntry() = runTest {
        val search = createLocalSearchDto("barbie")

        dao.upsertRecentSearch(search)
        val result = dao.getRecentSearches()

        assertThat(result.map { it.toComparable() }).contains(search.toComparable())
    }

    @Test
    fun upsertRecentSearch_shouldUpdateExistingEntry() = runTest {
        val original = createLocalSearchDto("oppenheimer")
        val updated = createLocalSearchDto("oppenheimer")

        dao.upsertRecentSearch(original)
        dao.upsertRecentSearch(updated)
        val result = dao.getRecentSearches()

        assertThat(result.map { it.toComparable() }).containsExactly(updated.toComparable())
    }


    @Test
    fun deleteAllSearches_shouldRemoveAll() = runTest {
        dao.upsertRecentSearch(createLocalSearchDto("foo"))
        dao.upsertRecentSearch(createLocalSearchDto("bar"))

        dao.deleteAllSearches()
        val result = dao.getRecentSearches()

        assertThat(result).isEmpty()
    }

    @Test
    fun deleteSearchByKeyword_shouldRemoveCorrectSearch() = runTest {
        val deletedSearch = createLocalSearchDto("this search will be delete")
        val otherSearch = createLocalSearchDto("other search")
        dao.upsertRecentSearch(deletedSearch)
        dao.upsertRecentSearch(otherSearch)

        dao.deleteSearchByKeyword(deletedSearch.searchKeyword)
        val result = dao.getRecentSearches()

        assertThat(result.map { it.toComparable() }).containsExactly(otherSearch.toComparable())
    }

    private data class ComparableSearchDto(
        val searchKeyword: String,
        val dateAdded: Long
    )

    private fun LocalSearchDto.toComparable(): ComparableSearchDto {
        return ComparableSearchDto(
            searchKeyword = this.searchKeyword,
            dateAdded = this.dateAdded.toEpochMilliseconds()
        )
    }
}

private fun createLocalSearchDto(
    keyword: String,
    dateAdded: Instant = Clock.System.now(),
): LocalSearchDto {
    return LocalSearchDto(
        searchKeyword = keyword,
        dateAdded = dateAdded
    )
}
