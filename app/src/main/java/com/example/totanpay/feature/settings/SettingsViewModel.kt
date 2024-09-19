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
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                deviceRepository.enableHome()
                _uiState.update { it.copy(showExitPasswordDialog = false, isExit = true) }
            }
            else {
                _uiState.update { it.copy(existPasswordError = "رمز اشتباه است.") }
            }
        }
    }
    fun checkSupervisorPassword(enteredPass: String) {
        viewModelScope.launch {
            if (supervisorSettingsRepository.supervisorPasswordIsValid(enteredPass)) {
                _uiState.update {
                    it.copy(
                        showSupervisorPasswordDialog = false,
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
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
            {
                _uiState.update {
                    it.copy(
                        merchantPasswordVerified = true,
                        showMerchantPasswordDialog = false
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
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass))
                _uiState.update {
                    it.copy(
                        reportPasswordVerified = true,
                        showReportPasswordDialog = false
                    )
                }
            else {
                _uiState.update { it.copy(reportPasswordError = "رمز اشتباه است.") }
            }
        }
    }

    fun showGetMerchantPasswordDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showMerchantPasswordDialog = true,
                    merchantPasswordError = ""
                )
            }
        }

    }

    fun hideGetMerchantPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showMerchantPasswordDialog = false) }
        }

    }

    fun showGetSupervisorPasswordDialog() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showSupervisorPasswordDialog = true,
                    supervisorPasswordError = ""
                )
            }
        }
    }

    fun hideGetSupervisorPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showSupervisorPasswordDialog = false) }
        }
    }

    fun showGetExitPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showExitPasswordDialog = true, existPasswordError = "") }
        }
    }

    fun hideGetExitPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showExitPasswordDialog = false) }
        }
    }

    fun showGetReportPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showReportPasswordDialog = true, reportPasswordError = "") }
        }
    }

    fun hideGetReportPasswordDialog() {
        viewModelScope.launch {
            _uiState.update { it.copy(showReportPasswordDialog = false) }
        }
    }

}

data class SettingsUiState(
    val showSupervisorPasswordDialog: Boolean = false,
    val showMerchantPasswordDialog: Boolean = false,
    val showReportPasswordDialog: Boolean = false,
    val showExitPasswordDialog: Boolean = false,
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

