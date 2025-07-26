package com.amsterdam.localdatasource.roomDataBase.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

class LocalDateConverter {
    @TypeConverter
    fun localDateToString(localDate: LocalDate): String {
        return if (localDate == LocalDate.fromEpochDays(0)) "" else localDate.toString()
    }

    @TypeConverter
    fun stringToLocalDate(dateString: String): LocalDate {
        return dateString.toLocalDate()
    }
}