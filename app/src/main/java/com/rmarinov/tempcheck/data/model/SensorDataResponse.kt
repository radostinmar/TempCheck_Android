package com.rmarinov.tempcheck.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SensorDataResponse(
    @Json(name = "humidity")
    val humidity: Float,
    @Json(name = "temperature")
    val temperature: Float
)