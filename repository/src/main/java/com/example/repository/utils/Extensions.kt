package com.example.repository.utils

import com.example.domain.exceptions.AflamiException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.format.DateTimeParseException

internal suspend fun <T, R> tryToExecute(
    function: suspend () -> T,
    onSuccess: suspend (T) -> R,
    onFailure: (AflamiException) -> R,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): R = withContext(dispatcher) {
    try {
        val result = function()
        onSuccess(result)
    } catch (exception: AflamiException) {
        onFailure(exception)
    } catch (exception: Exception) {
        onFailure(AflamiException())
    }
}

fun String.toSafeLocalDate(): LocalDate{
    return try {
        this.toLocalDate()
    } catch (e: Exception){
        LocalDate.fromEpochDays(0)
    }
}