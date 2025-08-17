package com.amsterdam.remotedatasource.utils.apiHandler

import com.amsterdam.domain.exceptions.AccessDeniedException
import com.amsterdam.domain.exceptions.AccessRestrictedException
import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.InvalidSessionException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.ServerErrorException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import com.amsterdam.domain.logger.Logger
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <reified T> responseCall(
    logger: Logger,
    crossinline execute: suspend () -> T,
    onHttpError: (String) -> Int? = { null },
): T =
    try {
        val result = execute()
        logger.debug("Result: $result")
        result
    } catch (e: HttpException) {
        throw if (e.code() == 401) {
            val serverResponse = e.response()?.errorBody()?.string() ?: throw UnknownException()
            mapHttpCodeToDomainException(onHttpError(serverResponse), logger, e)
        } else {
            logger.error("HttpException", e)
            ServerErrorException()
        }
    } catch (e: ConnectException) {
        logger.error("ConnectException: No internet connection", e)
        throw NoInternetException()
    } catch (e: SerializationException) {
        logger.error("SerializationException: Serialization error", e)
        throw ServerErrorException()
    } catch (e: UnknownHostException) {
        logger.error("UnknownHostException: No internet ", e)
        throw NoInternetException()
    } catch (e: SocketTimeoutException) {
        logger.error("SocketTimeoutException: No internet", e)
        throw NetworkException()
    } catch (e: Exception) {
        logger.errorWithCrashlytics("Exception: Unknown error", e)
        throw NetworkException()
    }

fun mapHttpCodeToDomainException(
    code: Int?,
    logger: Logger,
    exception: HttpException
): NetworkException =
    when (code) {
        3, 14, 30 -> {
            logger.error("HttpException: Invalid credentials", exception)
            InvalidCredentialsException()
        }

        7, 10, 16, 17, 33, 35, 36 -> {
            logger.error("HttpException: Invalid session", exception)
            InvalidSessionException()
        }

        31 -> {
            logger.error("HttpException: Account disabled", exception)
            AccountDisabledException()
        }

        32 -> {
            logger.error("HttpException: Verification required", exception)
            VerificationRequiredException()
        }

        38, 39 -> {
            logger.error("HttpException: Access denied", exception)
            AccessDeniedException()
        }

        45 -> {
            logger.error("HttpException: Access restricted", exception)
            AccessRestrictedException()
        }

        else -> {
            logger.errorWithCrashlytics("HttpException: Unknown error", exception)
            NetworkException()
        }
    }
