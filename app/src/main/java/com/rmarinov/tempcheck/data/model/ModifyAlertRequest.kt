package com.rmarinov.tempcheck.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModifyAlertRequest(
    @Json(name = "active")
    val active: Boolean
)