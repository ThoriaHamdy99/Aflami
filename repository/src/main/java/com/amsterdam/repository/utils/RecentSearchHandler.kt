package com.amsterdam.repository.utils

import com.amsterdam.repository.dto.local.utils.SearchType

interface RecentSearchHandler {
    suspend fun isRecentSearchExpired(keyword: String, searchType: SearchType, storedLanguage: String): Boolean
    suspend fun deleteRecentSearch(keyword: String, searchType: SearchType, storedLanguage: String)
}