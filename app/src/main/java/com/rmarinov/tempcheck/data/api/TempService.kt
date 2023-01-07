package com.rmarinov.tempcheck.data.api

import com.rmarinov.tempcheck.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface TempService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): TokenResponse

    @GET("current")
    suspend fun getCurrentData(): SensorDataResponse

    @GET("history")
    suspend fun getDataHistory(
        @Query("period") period: Period
    ): List<SensorDataHistoryResponse>

    @GET("alerts")
    suspend fun getAlerts(
        @Header("Authorization") authorization: String
    ): List<AlertResponse>

    @POST("alerts")
    suspend fun createAlert(
        @Header("Authorization") authorization: String,
        @Body alert: AlertRequest
    ): AlertResponse

    @PATCH("alerts/{id}")
    suspend fun changeAlertState(
        @Header("Authorization") authorization: String,
        @Path("id") alertId: Int,
        @Body modifyAlertRequest: ModifyAlertRequest
    ): AlertResponse

    @DELETE("alerts/{id}")
    suspend fun deleteAlert(
        @Header("Authorization") authorization: String,
        @Path("id") alertId: Int
    ): Response<Unit>

    @POST("token")
    suspend fun sendToken(
        @Header("Authorization") authorization: String,
        @Body token: TokenRequest
    ): Response<Unit>
}