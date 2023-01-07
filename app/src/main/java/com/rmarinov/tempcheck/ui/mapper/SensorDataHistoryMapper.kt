package com.rmarinov.tempcheck.ui.mapper

import android.content.res.Resources
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.data.model.SensorDataHistoryResponse
import com.rmarinov.tempcheck.ui.model.SensorDataHistoryUiModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SensorDataHistoryMapper @Inject constructor(
    private val resources: Resources
) {

    fun mapToUiModel(response: List<SensorDataHistoryResponse>): SensorDataHistoryUiModel {
        val temperatures = response.map { it.temperature }

        val maxTemp = temperatures.max().let {
            resources.getString(R.string.max_temperature, it)
        }

        val minTemp = temperatures.min().let {
            resources.getString(R.string.min_temperature, it)
        }

        val avgTemp = temperatures.average().let {
            resources.getString(R.string.avg_temperature, it)
        }

        val dates = response.map { it.timestamp }

        val startDate = LocalDateTime.parse(dates.first(), serverDateTimeFormatter)
            .formatForDisplaying()
            .let {
                resources.getString(R.string.start_date, it)
            }

        val endDate = LocalDateTime.parse(dates.last(), serverDateTimeFormatter)
            .formatForDisplaying()
            .let {
                resources.getString(R.string.end_date, it)
            }

        return SensorDataHistoryUiModel(maxTemp, minTemp, avgTemp, startDate, endDate)
    }

    private fun LocalDateTime.formatForDisplaying(): String =
        atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(displayDateTimeFormatter)

    companion object {

        private val serverDateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME

        private val displayDateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")
    }
}