package com.rmarinov.tempcheck.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertRequest(
    @Json(name = "direction")
    val direction: Direction,
    @Json(name = "target")
    val target: Float
)