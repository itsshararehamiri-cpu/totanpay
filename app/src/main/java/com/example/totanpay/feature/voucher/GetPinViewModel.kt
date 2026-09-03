package com.example.totanpay.feature.voucher


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.feature.purchase.GetPinUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPinViewModel @Inject constructor(private val deviceSettingsRepository: DeviceSettingsRepository
,   private val deviceRepository: DeviceRepository
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(GetPinUiState())
    val uiState: StateFlow<GetPinUiState> = _uiState
    fun getPin(track2: String,context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(playbackSound=deviceSettingsRepository.getPlaybackStatusSound()) }
            val string: List<String> = track2.split("=")
            val pan = string[0]
            deviceRepository.getPinBlock(context = context,
                pan,
                onError = { println("onError->$it") },
                onInput = { println("onInput->$it") },
                onConfirm = { pinBlock ->
                    viewModelScope.launch {
                        _uiState.update { it.copy(getPin = true, pinBlock = pinBlock) }
                    }
                },
                onCancel = {
                    _uiState.update { it.copy(isCancel = true) }
                },
                onTimeOut = {
                    println("onTimeOut->")
                    _uiState.update { it.copy(isCancel = true) }
                })
        }
    }

}

