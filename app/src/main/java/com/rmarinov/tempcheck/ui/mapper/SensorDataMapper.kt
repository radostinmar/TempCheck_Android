package com.rmarinov.tempcheck.ui.mapper

import android.content.res.Resources
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.data.model.SensorDataResponse
import com.rmarinov.tempcheck.ui.model.SensorDataUiModel
import javax.inject.Inject

class SensorDataMapper @Inject constructor(
    private val resources: Resources
) {

    fun mapToUiModel(response: SensorDataResponse): SensorDataUiModel =
        SensorDataUiModel(
            temp = resources.getString(R.string.temperature, response.temperature),
            humidity = resources.getString(R.string.humidity, response.humidity)
        )
}