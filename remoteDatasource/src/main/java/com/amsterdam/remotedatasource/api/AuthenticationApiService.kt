package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApiService {
    @GET(REQUEST_TOKEN_URL)
    suspend fun createRequestToken(): AuthenticationResponseDto

    @POST(CREATE_SESSION_WITH_URL)
    suspend fun createSessionWithLogin(
        @Body createSessionRequest: CreateSessionDto,
    ): AuthenticationResponseDto

    @FormUrlEncoded
    @POST(CREATE_SESSION_URL)
    suspend fun createSession(
        @Field("request_token") requestToken: String,
    ): CreateSessionResponseDto


    @DELETE(DELETE_SESSION_URL)
    suspend fun deleteSession(
        @Field("session_id") sessionId: String
    ): AuthenticationResponseDto

    companion object {
        const val REQUEST_TOKEN_URL = "authentication/token/new"
        const val CREATE_SESSION_WITH_URL = "authentication/token/validate_with_login"
        const val CREATE_SESSION_URL = "authentication/session/new"
        const val DELETE_SESSION_URL = "authentication/session"
    }
}
