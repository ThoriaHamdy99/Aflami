package com.amsterdam.viewmodel.utils

import kotlinx.datetime.LocalDate
import java.util.Locale
import kotlin.math.roundToInt

    fun dateToString(date: LocalDate?): String {
        if (date == null) {
            return ""
        }
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year.toString()
        return "$day-$month-$year"
    }

    fun movieLengthToHourMinuteString(movieLength: Int): String {
        val hours = movieLength / 60
        val minutes = movieLength % 60
        return "${hours}h ${minutes}m"
    }
    fun formatDuration(duration: Int): String {
        val hours = duration / 60
        val minutes = duration % 60

        return when {
            hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
            hours > 0 -> "${hours}h"
            else -> "${minutes}m"
        }
    }