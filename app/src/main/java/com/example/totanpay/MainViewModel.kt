package com.example.totanpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val deviceSettingsRepository: DeviceSettingsRepository
) : ViewModel() {
    private val _dataFlow = MutableStateFlow(false)
    val dataFlow: StateFlow<Boolean> = _dataFlow

    init {
        viewModelScope.launch {
            deviceSettingsRepository.getThemeIsDark()?.collect {
                _dataFlow.emit(it)
            }
        }
    }
}





