package com.rmarinov.tempcheck.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.databinding.ActivityAlertsBinding
import com.rmarinov.tempcheck.ui.adapter.AlertsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertsBinding

    private val viewModel: AlertsViewModel by viewModels()

    private val adapter by lazy {
        AlertsAdapter(
            viewModel::onDeleteClicked,
            viewModel::onSwitchToggled
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityAlertsBinding>(this, R.layout.activity_alerts)
            .apply {
                rvAlerts.adapter = adapter
                btnCreateAlert.setOnClickListener {
                    viewModel.onCreateClicked()
                }
            }

        viewModel.error.observe(this, EventObserver { errorText ->
            Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
        })

        viewModel.alerts.observe(this) {
            adapter.submitList(it)
        }

        viewModel.openCreateAlert.observe(this, EventObserver {
            CreateAlertActivity.start(this)
        })

        viewModel.isLoading.observe(this) { isLoading ->
            binding.rvAlerts.isVisible = !isLoading
            binding.groupSkeleton.isVisible = isLoading
        }

        viewModel.openLogin.observe(this, EventObserver {
            LoginActivity.start(this)
            finish()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.init()
    }

    companion object {

        fun start(context: Context): Unit =
            Intent(context, AlertsActivity::class.java)
                .let(context::startActivity)
    }
}