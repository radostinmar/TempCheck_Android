package com.rmarinov.tempcheck.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.databinding.ActivityMainBinding
import com.rmarinov.tempcheck.ui.model.SensorDataHistoryUiModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                rgPeriod.setOnCheckedChangeListener { _, checkedId ->
                    viewModel.onPeriodChanged(checkedId)
                }
                btnAlerts.setOnClickListener {
                    viewModel.onAlertsButtonClicked()
                }
            }

        viewModel.currentData.observe(this) {
            binding.tvTemperature.text = it.temp
            binding.tvHumidity.text = it.humidity
        }

        viewModel.error.observe(this, EventObserver { errorText ->
            Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
        })

        viewModel.history.observe(this) {
            updateHistory(it)
        }

        viewModel.openLogin.observe(this, EventObserver {
            LoginActivity.start(this)
        })

        viewModel.openAlerts.observe(this, EventObserver {
            AlertsActivity.start(this)
        })

        viewModel.isHistoryLoading.observe(this) { isLoading ->
            binding.groupSkeletonHistory.isVisible = isLoading
            binding.groupHistory.isVisible = !isLoading
        }

        viewModel.isCurrentLoading.observe(this) { isLoading ->
            binding.groupSkeletonCurrent.isVisible = isLoading
            binding.groupCurrent.isVisible = !isLoading
        }

        viewModel.onCreate()
    }

    private fun updateHistory(history: SensorDataHistoryUiModel) {
        binding.apply {
            tvStartDate.text = history.startDate
            tvEndDate.text = history.endDate
            tvMinTemp.text = history.minTemp
            tvMaxTemp.text = history.maxTemp
            tvAvgTemp.text = history.avgTemp
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}