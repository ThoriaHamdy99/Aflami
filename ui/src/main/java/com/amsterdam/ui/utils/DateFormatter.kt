package com.amsterdam.ui.utils

import kotlinx.datetime.LocalDate

fun formateDateForDisplay(dateString: String): String {
    return try {
        val date = LocalDate.parse(dateString)
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year

        "$day-$month-$year"
    } catch (e: Exception) {
        dateString
    }
}