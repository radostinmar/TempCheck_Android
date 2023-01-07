package com.rmarinov.tempcheck.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmarinov.tempcheck.data.model.ModifyAlertRequest
import com.rmarinov.tempcheck.domain.repository.TempRepository
import com.rmarinov.tempcheck.ui.mapper.AlertMapper
import com.rmarinov.tempcheck.ui.model.AlertUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val tempRepository: TempRepository,
    private val defaultDispatcher: CoroutineDispatcher,
    private val alertMapper: AlertMapper
) : ViewModel() {

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _alerts = MutableLiveData<List<AlertUiModel>>()
    val alerts: LiveData<List<AlertUiModel>> = _alerts

    private val _openCreateAlert = MutableLiveData<Event<Unit>>()
    val openCreateAlert: LiveData<Event<Unit>> = _openCreateAlert

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _openLogin = MutableLiveData<Event<Unit>>()
    val openLogin: LiveData<Event<Unit>> = _openLogin

    fun init() {
        viewModelScope.launch(defaultDispatcher) {
            _isLoading.postValue(true)
            loadAlerts()
        }
    }

    private suspend fun loadAlerts() {
        runCatching {
            tempRepository.getAlerts()
                .map(alertMapper::toUiModel)
        }.onSuccess {
            _isLoading.postValue(false)
            _alerts.postValue(it)
        }.onFailure {
            _isLoading.postValue(false)
            if (it is HttpException && it.code() == HTTP_UNAUTHORIZED) {
                _openLogin.postValue(Event(Unit))
            } else {
                _error.postValue(Event("Error loading alerts."))
            }
        }
    }

    fun onDeleteClicked(alertId: Int) {
        viewModelScope.launch(defaultDispatcher) {
            runCatching {
                tempRepository.deleteAlert(alertId)
            }.onSuccess {
                loadAlerts()
            }.onFailure {
                _error.postValue(Event("Error deleting alert. Please try again."))
            }
        }
    }

    fun onSwitchToggled(alertId: Int, isActive: Boolean) {
        viewModelScope.launch(defaultDispatcher) {
            runCatching {
                tempRepository.changeAlertState(alertId, ModifyAlertRequest(isActive))
            }.onFailure {
                _error.postValue(Event("Error toggling alert. Please try again."))
            }
        }
    }

    fun onCreateClicked() {
        _openCreateAlert.value = Event(Unit)
    }
}