package com.example.totanpay.feature.settings.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaySoundSettingsViewModel @Inject constructor(
    private val repository: DeviceSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PlaySoundSettingsUiState())
    val uiState: StateFlow<PlaySoundSettingsUiState> = _uiState


    fun changePlaySoundStatus(status:Boolean) {
        viewModelScope.launch {
            repository.setPlaybackStatusSound(status)
            _uiState.update { it.copy(confirmSettings=true) }
        }
    }


}

data class PlaySoundSettingsUiState(
    val confirmSettings: Boolean = false,

)

