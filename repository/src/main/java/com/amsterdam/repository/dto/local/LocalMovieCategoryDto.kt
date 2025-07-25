package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.MOVIE_CATEGORY_TABLE,
    primaryKeys = ["categoryId", "storedLanguage"],
    indices = [Index(value = ["name"], unique = true)]
)
data class LocalMovieCategoryDto(
    val categoryId: Long,
    val storedLanguage: String,
    val name: String
)