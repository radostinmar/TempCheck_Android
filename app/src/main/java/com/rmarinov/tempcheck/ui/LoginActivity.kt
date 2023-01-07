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
import com.rmarinov.tempcheck.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding?>(this, R.layout.activity_login)
            .apply {
                btnContinue.setOnClickListener {
                    viewModel.onLoginClicked(
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                }

                etUsername.doAfterTextChanged { username ->
                    viewModel.onInputChanged(username.toString(), etPassword.text.toString())
                }

                etPassword.doAfterTextChanged { password ->
                    viewModel.onInputChanged(etUsername.text.toString(), password.toString())
                }

                tvRegister.setOnClickListener {
                    viewModel.onRegisterClicked()
                }
            }

        viewModel.openRegister.observe(this, EventObserver {
            RegisterActivity.start(this)
            finish()
        })

        viewModel.error.observe(this, EventObserver { errorText ->
            Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
        })

        viewModel.openAlerts.observe(this, EventObserver {
            AlertsActivity.start(this)
            finish()
        })

        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnContinue.isVisible = !isLoading
            binding.tvRegister.isVisible = !isLoading
            binding.skeletonBtnContinue.isVisible = isLoading
        }

        viewModel.isButtonEnabled.observe(this) { isButtonEnabled ->
            binding.btnContinue.isEnabled = isButtonEnabled
        }
    }

    companion object {

        fun start(context: Context): Unit =
            Intent(context, LoginActivity::class.java)
                .let(context::startActivity)
    }
}