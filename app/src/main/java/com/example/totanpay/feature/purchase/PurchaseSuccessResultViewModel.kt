package com.example.totanpay.feature.purchase

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.ResultTransactionUiState
import com.example.totanpay.data.repository.DeviceSettingsRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.PrintStatus
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.data.util.toEnglishNumber
import com.example.totanpay.util.convertPurchaseResultToJsonObject
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PurchaseSuccessResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository,
    private val deviceSettingsRepository: DeviceSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ResultTransactionUiState())
    val uiState: StateFlow<ResultTransactionUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    playbackSound = deviceSettingsRepository.getPlaybackStatusSound()
                )
            }
        }
    }

    fun init(response: String) {
        viewModelScope.launch {
            val result = Gson().fromJson(response, ResponseTransaction::class.java)
            var printStatus =
                printCustomerSettingsRepository.getPrintStatus()
            if (printStatus == PrintStatus.PRINTING_WITH_MIN_AMOUNT && printCustomerSettingsRepository.getMinimumAmountForPrint()
                    .  toEnglishNumber()  .toLong() <= result.amount.toEnglishNumber()
                    .toLong()
            ) {
                printStatus = PrintStatus.ALWAYS_PRINTING
            }
            _uiState.update {
                it.copy(
                    result = result.copy(date = getPersianDate(result.date)),
                    printStatus = printStatus
                )
            }
            mainRepository.settlementReverse()
        }
    }

    fun print(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            mainRepository.print(bitmap, context, onSuccess = {}, onFailed = {})
        }
    }

    fun changePrintStatus() {
        viewModelScope.launch {
            if (_uiState.value.result != null)
                if (_uiState.value.result!!.date != null && _uiState.value.result!!.time != null)
                    mainRepository.changePrintStatusOfTransactionInQueue(
                        _uiState.value!!.result!!.date,
                        _uiState.value.result!!.time
                    )
        }
    }

    fun getResponseFroCallerApp(response: String) {
        viewModelScope.launch {
            val result = convertPurchaseResultToJsonObject(response)
            _uiState.update { it.copy(responseForCallerApp = result) }
        }
    }

}
