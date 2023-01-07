package com.rmarinov.tempcheck.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlertResponse(
    @Json(name = "active")
    val active: Boolean,
    @Json(name = "direction")
    val direction: Direction,
    @Json(name = "id")
    val id: Int,
    @Json(name = "target")
    val target: Float
)