package com.rmarinov.tempcheck.ui.mapper

import android.content.res.Resources
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.data.model.AlertResponse
import com.rmarinov.tempcheck.data.model.Direction
import com.rmarinov.tempcheck.ui.model.AlertUiModel
import javax.inject.Inject

class AlertMapper @Inject constructor(private val resources: Resources) {

    fun toUiModel(alertResponse: AlertResponse): AlertUiModel =
        AlertUiModel(
            alertResponse.id,
            alertResponse.description,
            alertResponse.active
        )

    private val AlertResponse.description: String
        get() = when (direction) {
            Direction.OVER -> resources.getString(R.string.over)
            Direction.UNDER -> resources.getString(R.string.under)
        }.let {
            resources.getString(
                R.string.alert_text,
                it,
                resources.getString(R.string.temperature_format, target)
            )
        }
}