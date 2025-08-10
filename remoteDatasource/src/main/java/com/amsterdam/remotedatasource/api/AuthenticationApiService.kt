package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionResponseDto
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApiService {
    @GET("authentication/token/new")
    suspend fun createRequestToken(): AuthenticationResponseDto

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Body createSessionRequest: CreateSessionDto,
    ): AuthenticationResponseDto

    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun createSession(
        @Field("request_token") requestToken: String,
    ): CreateSessionResponseDto


}
