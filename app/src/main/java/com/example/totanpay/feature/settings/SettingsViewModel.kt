package com.example.totanpay.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.R
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.log.LogRepository
import com.example.totanpay.data.repository.log.LogType
import com.example.totanpay.data.repository.settings.CurrentLanguageRepository
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.settings.supervisor.SupervisorSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val merchantSettingsRepository: MerchantSettingsRepository,
    private val supervisorSettingsRepository: SupervisorSettingsRepository,
    private val deviceRepository: DeviceRepository,
    private val currentLanguageRepository: CurrentLanguageRepository,
    private val logRepository: LogRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        _uiState.update { it.copy(showKeyboard = true) }
    }
    fun checkExistPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(existPasswordError = R.string.empty_message) }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                deviceRepository.enableHome()
                _uiState.update { it.copy( isExit = true) }
            }
            else {
                logRepository.addLog(LogType.MERCHANT_PASSWORD_INCORRECT)
                _uiState.update { it.copy(existPasswordError =  R.string.pass_is_incorrect) }
            }
        }
    }
    fun checkSupervisorPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(supervisorPasswordError =R.string.empty_message) }
            if (supervisorSettingsRepository.supervisorPasswordIsValid(enteredPass)) {
                _uiState.update {
                    it.copy(
                        supervisorPasswordVerified = true
                    )
                }
            } else {
                logRepository.addLog(LogType.SUPERVISOR_PASSWORD_INCORRECT)
                _uiState.update { it.copy(supervisorPasswordError =R.string.pass_is_incorrect) }
            }
        }
    }
    fun checkMerchantPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(merchantPasswordError = R.string.empty_message) }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                _uiState.update {
                    it.copy(
                        merchantPasswordVerified = true
                    )
                }
            }
            else {
                logRepository.addLog(LogType.MERCHANT_PASSWORD_INCORRECT)
                _uiState.update { it.copy(merchantPasswordError =R.string.pass_is_incorrect) }
            }
        }
    }

    fun checkReportPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(reportPasswordError = R.string.empty_message) }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
                _uiState.update {
                    it.copy(
                        reportPasswordVerified = true
                    )
                }
            else {
                logRepository.addLog(LogType.MERCHANT_PASSWORD_INCORRECT)
                _uiState.update { it.copy(reportPasswordError = R.string.pass_is_incorrect) }
            }
        }
    }
    fun setCurrentLanguage(isFarsi: Boolean){
        viewModelScope.launch {
            currentLanguageRepository.setLanguage(isFarsi)
        }
    }
}

data class SettingsUiState(
    val supervisorPasswordError: Int=-1,
    val merchantPasswordError: Int=-1,
    val reportPasswordError: Int =-1,
    val existPasswordError: Int=-1,
    val isExit: Boolean = false,
    val merchantPasswordVerified: Boolean = false,
    val reportPasswordVerified: Boolean = false,
    val supervisorPasswordVerified: Boolean = false,
    val showKeyboard:Boolean=true,
    val currentLanguageIsFarsi: Boolean=false
)

