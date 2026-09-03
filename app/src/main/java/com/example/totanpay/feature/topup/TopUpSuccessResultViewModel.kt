package com.example.totanpay.feature.topup

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.ResultTransactionUiState
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepository
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.data.util.toEnglishNumber
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopUpSuccessResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository,
    private val deviceSettingsRepository: DeviceSettingsRepository,
    private val merchantSettingsRepository: MerchantSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ResultTransactionUiState())
    val uiState: StateFlow<ResultTransactionUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(playbackSound = deviceSettingsRepository.getPlaybackStatusSound()) }
        }
    }

    fun init(response: String) {
        viewModelScope.launch {
            val result = Gson().fromJson(response, ResponseTransaction::class.java)
            _uiState.update {
                it.copy(
                    result = result.copy(date = getPersianDate(result.date))
                )
            }
            mainRepository.settlementReverse()
        }
    }

    fun getPrintStatus() {
        val result = _uiState.value.result ?: return
        val printStatus = printCustomerSettingsRepository.getPrintStatus()
        val merchantStatus = merchantSettingsRepository.getPrinStatus()

        var showPrintForCustomer = false
        var shouldAutoPrintCustomerReceipt = false

        when (printStatus) {
            PrintStatus.NO_PRINTING -> {
                showPrintForCustomer = false
                shouldAutoPrintCustomerReceipt = false
                changePrintStatus()
            }
            PrintStatus.PRINT -> {
                val isAutoPrintEnabled = printCustomerSettingsRepository.getAutoPrintCustomerReceipt()
                if (!isAutoPrintEnabled) {
                    showPrintForCustomer = true
                    shouldAutoPrintCustomerReceipt = false
                } else {
                    val minAmountStr = printCustomerSettingsRepository.getMinimumAmountForPrint()
                        .trim().toEnglishNumber()
                    val transactionAmount = result.amount.toEnglishNumber().toLongOrNull() ?: 0L
                    val minAmount = minAmountStr.takeIf { it.isNotBlank() }?.toLongOrNull()

                    if (minAmount == null || transactionAmount >= minAmount) {
                        showPrintForCustomer = false
                        shouldAutoPrintCustomerReceipt = true
                    } else {
                        showPrintForCustomer = true
                        shouldAutoPrintCustomerReceipt = false
                    }
                }
            }
        }

        val showPrintForMerchant = merchantStatus == PrintStatus.PRINT

        _uiState.update {
            it.copy(
                customerPrintStatus = printStatus,
                merchantPrintStatus = merchantStatus,
                showPrintForCustomer = showPrintForCustomer,
                showPrintForMerchant = showPrintForMerchant,
                autoPrintCustomerReceipt = shouldAutoPrintCustomerReceipt
            )
        }
    }

    fun printCustomerReceipt(bitmap: Bitmap, context: Context) {
        _uiState.update { it.copy(showPrintForCustomer = false, autoPrintCustomerReceipt = false) }
        viewModelScope.launch {
            mainRepository.print(
                bitmap, context,
                onSuccess = {
                    viewModelScope.launch(Dispatchers.Main.immediate) {
                        changePrintStatus()
                    }
                },
                onFailed = { errorMessage ->
                    _uiState.update { it.copy(errorInPrint = errorMessage) }
                }
            )
        }
    }

    fun printMerchantReceipt(bitmap: Bitmap, context: Context) {
        _uiState.update { it.copy(showPrintForMerchant = false) }
        viewModelScope.launch {
            mainRepository.print(
                bitmap, context,
                onSuccess = { },
                onFailed = { errorMessage ->
                    _uiState.update { it.copy(errorInPrint = errorMessage) }
                }
            )
        }
    }

    fun changePrintStatus() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            try {
                val result = _uiState.value.result ?: return@launch
                val date = result.date ?: return@launch
                val time = result.time ?: return@launch
                mainRepository.changePrintStatusOfTransactionInQueue(date, time)
            } catch (_: Exception) {
                // جلوگیری از کرش؛ آپدیت وضعیت چاپ اختیاری است
            }
        }
    }

    fun clearErrorMessage() {
        viewModelScope.launch {
            _uiState.update { it.copy(errorInPrint = "") }
        }
    }
}