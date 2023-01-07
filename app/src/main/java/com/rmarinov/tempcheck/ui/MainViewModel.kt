package com.rmarinov.tempcheck.ui

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.data.model.Period
import com.rmarinov.tempcheck.domain.repository.TempRepository
import com.rmarinov.tempcheck.ui.mapper.SensorDataHistoryMapper
import com.rmarinov.tempcheck.ui.mapper.SensorDataMapper
import com.rmarinov.tempcheck.ui.model.SensorDataHistoryUiModel
import com.rmarinov.tempcheck.ui.model.SensorDataUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tempRepository: TempRepository,
    private val sensorDataMapper: SensorDataMapper,
    private val sensorDataHistoryMapper: SensorDataHistoryMapper,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val selectedPeriodFlow = MutableStateFlow(Period.DAY)

    private val _currentData = MutableLiveData<SensorDataUiModel>()
    val currentData: LiveData<SensorDataUiModel> = _currentData

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _history = MutableLiveData<SensorDataHistoryUiModel>()
    val history: LiveData<SensorDataHistoryUiModel> = _history

    private val _openLogin = MutableLiveData<Event<Unit>>()
    val openLogin: LiveData<Event<Unit>> = _openLogin

    private val _openAlerts = MutableLiveData<Event<Unit>>()
    val openAlerts: LiveData<Event<Unit>> = _openAlerts

    private val _isHistoryLoading = MutableLiveData<Boolean>()
    val isHistoryLoading: LiveData<Boolean> = _isHistoryLoading

    private val _isCurrentLoading = MutableLiveData<Boolean>()
    val isCurrentLoading: LiveData<Boolean> = _isCurrentLoading

    fun onCreate() {
        viewModelScope.launch(defaultDispatcher) {
            selectedPeriodFlow.collectLatest { period ->
                _isHistoryLoading.postValue(true)
                runCatching {
                    tempRepository.getDataHistory(period)
                        .let(sensorDataHistoryMapper::mapToUiModel)
                }.onSuccess {
                    _history.postValue(it)
                    _isHistoryLoading.postValue(false)
                }.onFailure {
                    if (it !is CancellationException) {
                        _error.postValue(Event("Error loading history."))
                    }
                    _isHistoryLoading.postValue(false)
                }
            }
        }
    }

    fun onResume() {
        viewModelScope.launch(defaultDispatcher) {
            _isCurrentLoading.postValue(true)
            runCatching {
                tempRepository.getCurrentData()
                    .let(sensorDataMapper::mapToUiModel)
            }.onSuccess {
                _currentData.postValue(it)
                _isCurrentLoading.postValue(false)
            }.onFailure {
                _isCurrentLoading.postValue(false)
            }
        }
    }

    fun onPeriodChanged(@IdRes buttonId: Int) {
        val period = when (buttonId) {
            R.id.rb_day -> Period.DAY
            R.id.rb_week -> Period.WEEK
            R.id.rb_month -> Period.MONTH
            R.id.rb_year -> Period.YEAR
            else -> throw IllegalStateException("Invalid radio button id")
        }
        selectedPeriodFlow.value = period
    }

    fun onAlertsButtonClicked() {
        viewModelScope.launch(defaultDispatcher) {
            runCatching {
                if (tempRepository.isLoggedIn()) {
                    _openAlerts.postValue(Event(Unit))
                } else {
                    _openLogin.postValue(Event(Unit))
                }
            }.onFailure { _error.postValue(Event("Unexpected error. Please try again.")) }
        }
    }
}