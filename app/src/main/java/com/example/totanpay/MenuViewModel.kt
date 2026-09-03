package com.example.totanpay

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.settings.supervisor.SupervisorSettingsRepository
import com.example.totanpay.data.util.formatTime
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.data.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MAXIMUM_AMOUNT_OF_TRANSACTION = "2000000000"

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val merchantSettingsRepository: MerchantSettingsRepository,
    private val mainRepository: MainRepository, private val deviceRepository: DeviceRepository,
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository,
    private val  supervisorSettingsRepository: SupervisorSettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState
    val hasSettleAdvice = mainRepository.hasTransactionInQueue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun init(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    merchantName = mainRepository.getMerchant()?.merchantName
                        ?: "",
                    englishMerchantName = mainRepository.getMerchant()?.englishMerchantName ?: "",
                    terminalId = mainRepository.getTerminalId() ?: "",
                    showProgress = false
                )
            }
            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            deviceRepository.disableHome()
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        _uiState.update {
                            it.copy(
                                merchantName = mainRepository.getMerchant()?.merchantName
                                    ?: "",
                                englishMerchantName = mainRepository.getMerchant()?.englishMerchantName
                                    ?: "",
                                terminalId = mainRepository.getTerminalId() ?: "",
                                showProgress = false, isSyncing = false
                            )
                        }
                        val temp = mainRepository.getNotPrintLastTransactionInQueue()
                        if (temp != null)
                            _uiState.update {
                                it.copy(
                                    lastTransactionIsNotPrinted = temp.copy(
                                        date = getPersianDate(temp.date),
                                        transactionType = TransactionType.titleOf(temp.transactionType).title,
                                        time = temp.time.formatTime()
                                    )
                                )
                            }
                        sendTransactionsInQueue()
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update {
                    it.copy(
                        configurationIsNotCompletedMessage = context.getString(
                            R.string.configuration_not_done
                        )
                    )
                }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun needToCheckPrinter(): Boolean {
        var flag = true
        if (printCustomerSettingsRepository.getPrintStatus() == PrintStatus.NO_PRINTING
            && merchantSettingsRepository.getPrinStatus() == PrintStatus.NO_PRINTING
        ) {
            flag = false
        }
        return flag
    }

    fun purchase(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showBatteryStatusMessage = false,
                    printerError = "",
                    showPrinterError = false
                )
            }
            if (isNetworkAvailable(context)) {
                val isConfigured = mainRepository.isConfigured()
                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        if (!merchantSettingsRepository.needToApportionments()) {
                            deviceRepository.disableHome()
                            if (!needToCheckPrinter()) {
                                _uiState.update { it.copy(purchase = true) }
                            } else {
                                deviceRepository.getPrinterError { error ->
                                    if (error.isNotEmpty()) {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = true,
                                                printerError = error,
                                                purchase = false,
                                                selectedTransaction = TransactionType.PURCHASE
                                            )
                                        }
                                    } else {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = false,
                                                printerError = "",
                                                purchase = true
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update {
                    it.copy(
                        configurationIsNotCompletedMessage = context.getString(
                            R.string.configuration_not_done
                        )
                    )
                }
            } else {
                _uiState.update { it.copy(showInternetIsNotAvailableMessage = true) }
            }
        }
    }

    fun balance(context: Context) {
        Log.d("TAG", "balance: dddddd")
        viewModelScope.launch {
            Log.d("TAG", "balance: dddddda")

            _uiState.update { it.copy(showBatteryStatusMessage = false) }
            Log.d("TAG", "balance: ddddddb")

            if (isNetworkAvailable(context)) {
                Log.d("TAG", "balance: ddddddc")

                val isConfigured = mainRepository.isConfigured()
                Log.d("TAG", "balance: ddddddd$isConfigured")
                Log.d("TAG", "balance: ddddddd${deviceRepository.batteryIsEnough()}")

                if (isConfigured) {
                    if (deviceRepository.batteryIsEnough()) {
                        deviceRepository.disableHome()
                        if (!needToCheckPrinter()) {
                            _uiState.update { it.copy(balance = true) }
                        } else {
                            deviceRepository.getPrinterError { error ->
                                if (error.isNotEmpty()) {
                                    _uiState.update {
                                        it.copy(
                                            showPrinterError = true,
                                            printerError = error,
                                            balance = false,
                                            selectedTransaction = TransactionType.BALANCE
                                        )
                                    }
                                } else {
                                    _uiState.update {
                                        it.copy(
                                            showPrinterError = false,
                                            printerError = "",
                                            balance = true
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update {
                    it.copy(
                        configurationIsNotCompletedMessage =
                            context.getString(R.string.configuration_not_done)
                    )
                }
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
                            if (!needToCheckPrinter()) {
                                _uiState.update { it.copy(voucher = true) }
                            } else {
                                deviceRepository.getPrinterError { error ->
                                    if (error.isNotEmpty()) {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = true,
                                                printerError = error,
                                                voucher = false,
                                                selectedTransaction = TransactionType.VOUCHER
                                            )
                                        }
                                    } else {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = false,
                                                printerError = "",
                                                voucher = true
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            configurationIsNotCompletedMessage = context.getString(
                                R.string.configuration_not_done
                            )
                        )
                    }
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
                            if (!needToCheckPrinter()) {
                                _uiState.update { it.copy(topUp = true) }
                            } else {
                                deviceRepository.getPrinterError { error ->
                                    if (error.isNotEmpty()) {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = true,
                                                printerError = error,
                                                topUp = false,
                                                selectedTransaction = TransactionType.TOPUP
                                            )
                                        }
                                    } else {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = false,
                                                printerError = "",
                                                topUp = true
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update {
                    it.copy(
                        configurationIsNotCompletedMessage = context.getString(
                            R.string.configuration_not_done
                        )
                    )
                }
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
                            if (!needToCheckPrinter()) {
                                _uiState.update { it.copy(billPay = true) }
                            } else {
                                deviceRepository.getPrinterError { error ->
                                    if (error.isNotEmpty()) {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = true,
                                                printerError = error,
                                                billPay = false,
                                                selectedTransaction = TransactionType.BILL_PAY
                                            )
                                        }
                                    } else {
                                        _uiState.update {
                                            it.copy(
                                                showPrinterError = false,
                                                printerError = "",
                                                billPay = true
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(showMessageNeedToSetApportionment = true) }
                        }
                    } else {
                        _uiState.update { it.copy(showBatteryStatusMessage = true) }
                    }
                } else _uiState.update {
                    it.copy(
                        configurationIsNotCompletedMessage = context.getString(
                            R.string.configuration_not_done
                        )
                    )
                }
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

    fun checkExistPassword(enteredPass: String, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(existPasswordError = "") }
            if (supervisorSettingsRepository.supervisorPasswordIsValid(enteredPass)) {
                deviceRepository.enableHome()
                _uiState.update { it.copy(isExit = true) }
            } else {
                _uiState.update { it.copy(existPasswordError = context.getString(R.string.pass_is_incorrect)) }
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

    fun sendAdviceReverse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            sendTransactionsInQueue()
            _uiState.update { it.copy(isSyncing = false) }

        }
    }

    fun continueTransaction() {
        if (_uiState.value.selectedTransaction == TransactionType.PURCHASE) {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", purchase = true
                )
            }
        } else if (_uiState.value.selectedTransaction == TransactionType.BALANCE) {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", balance = true
                )
            }
        } else if (_uiState.value.selectedTransaction == TransactionType.VOUCHER) {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", voucher = true
                )
            }
        } else if (_uiState.value.selectedTransaction == TransactionType.TOPUP) {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", topUp = true
                )
            }
        } else if (_uiState.value.selectedTransaction == TransactionType.BILL_PAY) {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", billPay = true
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    showPrinterError = false, printerError = "", selectedTransaction = null
                )
            }
        }
    }

    fun cancelTransaction() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showPrinterError = false,
                    printerError = "",
                    purchase = false,
                    balance = false,
                    billPay = false,
                    voucher = false,
                    topUp = false,
                    selectedTransaction = null
                )
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
    val englishMerchantName: String = "",
    val terminalId: String = "",
    val showBatteryStatusMessage: Boolean = false,
    val lastTransactionIsNotPrinted: ResponseTransaction? = null,
    val showProgress: Boolean = false,
    val showInternetIsNotAvailableMessage: Boolean = false,
    val showSwitchIsNotAvailableMessage: Boolean = false,
    val existPasswordError: String = "",
    val isExit: Boolean = false,
    val showMessageNeedToSetApportionment: Boolean = false,
    val maximumAmountForPurchaseTransaction: String = MAXIMUM_AMOUNT_OF_TRANSACTION,
    val isSyncing: Boolean = false, val showPrinterError: Boolean = false,
    val printerError: String = "",
    val selectedTransaction: TransactionType? = null
)
