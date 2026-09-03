package com.example.totanpay.feature.settings.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MerchantSettingsViewModel @Inject constructor(
    private val deviceSettingsRepository: DeviceSettingsRepository,
    private val repository: DeviceSettingsRepository,
    private val merchantRepository: MerchantSettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MerchantSettingsUiState())
    val uiState: StateFlow<MerchantSettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(playSoundIsEnabled = repository.getPlaybackStatusSound(),
                shouldChangePassword = !merchantRepository.isChangePassword()) }
                deviceSettingsRepository.getThemeIsDark()?.collect{themeIsDark->
                    _uiState.update {  it.copy(themeIsDark =themeIsDark)
                }

            }
        }
    }
   fun changeTheme(themeIsDark:Boolean){
       viewModelScope.launch {
           deviceSettingsRepository.setThemeIsDark(themeIsDark)
           _uiState.update {
               it.copy(themeIsDark =themeIsDark)
           }
       }
   }
    fun changePlaySoundStatus(status:Boolean) {
        viewModelScope.launch {
            repository.setPlaybackStatusSound(status)
            _uiState.update { it.copy(confirmSettings=true) }
        }
    }

}
data class MerchantSettingsUiState(
    val themeIsDark:Boolean=false,
    val confirmSettings: Boolean = false,
    val playSoundIsEnabled:Boolean=false,
    val shouldChangePassword: Boolean=false)




