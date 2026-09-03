package com.example.totanpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.domain.GetCurrentLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val deviceSettingsRepository: DeviceSettingsRepository,
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase
) : ViewModel() {
    val dataFlow: StateFlow<Boolean> = deviceSettingsRepository.getThemeIsDark()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val isFarsiSelected: StateFlow<Boolean> = getCurrentLanguageUseCase.invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
            true)


}





