package com.rmarinov.tempcheck.ui.model

data class SensorDataHistoryUiModel(
    val maxTemp: String,
    val minTemp: String,
    val avgTemp: String,
    val startDate: String,
    val endDate: String
)