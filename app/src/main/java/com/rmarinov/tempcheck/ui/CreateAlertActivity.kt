package com.rmarinov.tempcheck.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.databinding.ActivityCreateAlertBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAlertBinding

    private val viewModel: CreateAlertViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCreateAlertBinding?>(this, R.layout.activity_create_alert)
            .apply {
                etTarget.doAfterTextChanged {
                    viewModel.onInputChanged(it.toString())
                }

                btnCreate.setOnClickListener {
                    viewModel.onCreateClicked(etTarget.text.toString())
                }
            }
        viewModel.error.observe(this, EventObserver { errorText ->
            Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
        })

        viewModel.finish.observe(this) {
            finish()
        }

        viewModel.isButtonEnabled.observe(this) { isButtonEnabled ->
            binding.btnCreate.isEnabled = isButtonEnabled
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnCreate.isVisible = !isLoading
            binding.skeletonBtnCreate.isVisible = isLoading
        }
    }

    companion object {

        fun start(context: Context): Unit =
            Intent(context, CreateAlertActivity::class.java)
                .let(context::startActivity)
    }
}