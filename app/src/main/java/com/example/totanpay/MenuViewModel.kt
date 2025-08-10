package com.example.totanpay

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.MerchantSettingsRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAXIMUM_AMOUNT_OF_TRANSACTION = "2000000000"

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val merchantSettingsRepository: MerchantSettingsRepository,

    private val mainRepository: MainRepository, private val deviceRepository: DeviceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState

    fun init(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    merchantName = mainRepository.getMerchant()?.merchantName ?: "",
                    terminalId = mainRepository.getTerminalId() ?: "",
                    showProgress = false
                )
            }
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            deviceRepository.enableHome()
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        _uiState.update {
                            it.copy(
                                merchantName = mainRepository.getMerchant()?.merchantName ?: "",
                                terminalId = mainRepository.getTerminalId() ?: "",
                                showProgress = false
                            )
                        }
                        sendTransactionsInQueue()
                        _uiState.update {
                            it.copy(
                                lastTransactionIsNotPrinted = mainRepository.getNotPrintLastTransactionInQueue()
                            )
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun purchase(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        if (!merchantSettingsRepository.needToApportionments()) {
                            deviceRepository.disableHome()
                            _uiState.update { it.copy(purchase = true) }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun balance(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        deviceRepository.disableHome()
                        _uiState.update { it.copy(balance = true) }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun voucher(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        if (!merchantSettingsRepository.needToApportionments()) {
                            deviceRepository.disableHome()

                            _uiState.update { it.copy(voucher = true) }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else {
                    _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
                }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun topUp(context: Context) {
        _uiState.update { it.copy(showBatteryStatusMessage = false) }
        viewModelScope.launch {
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        if (!merchantSettingsRepository.needToApportionments()) {
                            deviceRepository.disableHome()
                            _uiState.update { it.copy(topUp = true) }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun billPay(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        if (!merchantSettingsRepository.needToApportionments()) {
                            deviceRepository.disableHome()

                            _uiState.update { it.copy(billPay = true) }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update { it.copy(configurationIsNotCompletedMessage = "پیکربندی انجام نشده است") }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }

            }
        }
    }

    fun hideConfigurationIsNotCompletedMessage() {
        viewModelScope.launch {
            _uiState.update { it.copy(configurationIsNotCompletedMessage = "") }
        }
    }

    private suspend fun sendTransactionsInQueue() {
        mainRepository.settlementReverse()
    }

    fun hideSwitchIsNotAvailableMessage() {
        viewModelScope.launch {
            _uiState.update { it.copy(showSwitchIsNotAvailableMessage = false) }
        }
    }

    fun hideInternetIsNotAvailableMessage() {
        viewModelScope.launch {
            _uiState.update { it.copy(showInternetIsNotAvailableMessage = false) }
        }
    }

    fun print(bitmap: Bitmap, context: Context, onSuccess: () -> Unit, onFailed: (String) -> Unit) {
        viewModelScope.launch {
            deviceRepository.print(
                bitmap,
                context,
                onSuccess = { onSuccess() },
                onFailed = { onFailed(it) })
        }
    }

    fun checkExistPassword(enteredPass: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(existPasswordError = "") }
            if (merchantSettingsRepository.merchantPasswordIsValid(enteredPass)) {
                deviceRepository.enableHome()
                _uiState.update { it.copy(isExit = true) }
            } else {
                _uiState.update { it.copy(existPasswordError = "رمز اشتباه است.") }
            }
        }
    }

    fun hideMessageNeedToSetApportionment() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(showMessageNeedToSetApportionment = false)
            }
        }
    }
}

data class MenuUiState(
    val configurationIsNotCompletedMessage: String = "",
    val haveErrorConnection: Boolean = false,
    val purchase: Boolean = false,
    val balance: Boolean = false,
    val billPay: Boolean = false,
    val voucher: Boolean = false,
    val topUp: Boolean = false,
    val merchantName: String = "",
    val terminalId: String = "",
    val showBatteryStatusMessage: Boolean = false,
    val lastTransactionIsNotPrinted: ResponseTransaction? = null,
    val showProgress: Boolean = false,
    val showInternetIsNotAvailableMessage: Boolean = false,
    val showSwitchIsNotAvailableMessage: Boolean = false,
    val existPasswordError: String = "",
    val isExit: Boolean = false,
    val showMessageNeedToSetApportionment: Boolean = false,
    val maximumAmountForPurchaseTransaction: String = MAXIMUM_AMOUNT_OF_TRANSACTION
)
