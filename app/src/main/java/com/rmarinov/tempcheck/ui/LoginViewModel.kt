package com.rmarinov.tempcheck.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmarinov.tempcheck.domain.repository.TempRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tempRepository: TempRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _openAlerts = MutableLiveData<Event<Unit>>()
    val openAlerts: LiveData<Event<Unit>> = _openAlerts

    private val _openRegister = MutableLiveData<Event<Unit>>()
    val openRegister: LiveData<Event<Unit>> = _openRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    fun onLoginClicked(username: String, password: String) {
        viewModelScope.launch(defaultDispatcher) {
            _isLoading.postValue(true)
            runCatching {
                tempRepository.login(username, password)
                tempRepository.sendToken()
            }.onSuccess {
                _isLoading.postValue(false)
                _openAlerts.postValue(Event(Unit))
            }.onFailure {
                _isLoading.postValue(false)
                _error.postValue(Event("Error logging in. The username or password may be wrong. Please try again."))
            }
        }
    }

    fun onInputChanged(username: String, password: String) {
        _isButtonEnabled.value = username.isNotBlank() && password.isNotBlank()
    }

    fun onRegisterClicked() {
        _openRegister.value = Event(Unit)
    }
}