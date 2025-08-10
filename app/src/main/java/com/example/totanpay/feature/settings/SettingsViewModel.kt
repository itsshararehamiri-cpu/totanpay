package com.example.totanpay.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MerchantSettingsRepository
import com.example.totanpay.data.repository.SupervisorSettingsRepository
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
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState
    init {
        _uiState.update { it.copy(showKeyboard = true) }
    }
    fun checkExistPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(existPasswordError = "") }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                deviceRepository.enableHome()
                _uiState.update { it.copy( isExit = true) }
            }
            else {
                _uiState.update { it.copy(existPasswordError = "رمز اشتباه است.") }
            }
        }
    }
    fun checkSupervisorPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(supervisorPasswordError = "") }
            if (supervisorSettingsRepository.supervisorPasswordIsValid(enteredPass)) {
                _uiState.update {
                    it.copy(
                        supervisorPasswordVerified = true
                    )
                }
            } else {
                _uiState.update { it.copy(supervisorPasswordError = "رمز اشتباه است.") }
            }
        }
    }
    fun checkMerchantPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(merchantPasswordError = "") }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                _uiState.update {
                    it.copy(
                        merchantPasswordVerified = true
                    )
                }
            }
            else {
                _uiState.update { it.copy(merchantPasswordError = "رمز اشتباه است.") }
            }
        }
    }

    fun checkReportPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(reportPasswordError = "") }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
                _uiState.update {
                    it.copy(
                        reportPasswordVerified = true
                    )
                }
            else {
                _uiState.update { it.copy(reportPasswordError = "رمز اشتباه است.") }
            }
        }
    }
}

data class SettingsUiState(
    val supervisorPasswordError: String = "",
    val merchantPasswordError: String = "",
    val reportPasswordError: String = "",
    val existPasswordError: String = "",
    val isExit: Boolean = false,
    val merchantPasswordVerified: Boolean = false,
    val reportPasswordVerified: Boolean = false,
    val supervisorPasswordVerified: Boolean = false,
    val showKeyboard:Boolean=true
)

