package com.rmarinov.tempcheck.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmarinov.tempcheck.data.model.AlertRequest
import com.rmarinov.tempcheck.data.model.Direction
import com.rmarinov.tempcheck.domain.repository.TempRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAlertViewModel @Inject constructor(
    private val tempRepository: TempRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _error = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> = _error

    private val _finish = MutableLiveData<Event<Unit>>()
    val finish: LiveData<Event<Unit>> = _finish

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> = _isButtonEnabled

    fun onCreateClicked(targetString: String) {
        viewModelScope.launch(defaultDispatcher) {
            _isLoading.postValue(true)
            runCatching {
                val currentTemperature = tempRepository.getCurrentData().temperature
                val target = targetString.toFloat()
                val direction = if (target >= currentTemperature) {
                    Direction.OVER
                } else {
                    Direction.UNDER
                }
                tempRepository.createAlert(AlertRequest(direction, target))
            }.onSuccess {
                _isLoading.postValue(false)
                _finish.postValue(Event(Unit))
            }.onFailure {
                _isLoading.postValue(false)
                _error.postValue(Event("Error. Please try again."))
            }
        }
    }

    fun onInputChanged(input: String) {
        _isButtonEnabled.value =
            input.toFloatOrNull()?.let { it < 50f } ?: false
    }
}