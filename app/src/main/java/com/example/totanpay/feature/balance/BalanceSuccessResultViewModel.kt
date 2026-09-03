package com.example.totanpay.feature.balance

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.PrintableViewModel
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.getPersianDate
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BalanceSuccessResultViewModel @Inject constructor(
    private val deviceSettingsRepository: DeviceSettingsRepository,
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository,
    override val deviceRepository: DeviceRepository
) : ViewModel() , PrintableViewModel {
    private val _uiState = MutableStateFlow(BalanceSuccessResultUiState())
    val uiState: StateFlow<BalanceSuccessResultUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(playbackSound = deviceSettingsRepository.getPlaybackStatusSound()) }
        }
    }

    fun init(response: String) {
        viewModelScope.launch {
            val result = Gson().fromJson(response, ResponseTransaction::class.java)
            var printStatus = printCustomerSettingsRepository.getPrintStatus()
            _uiState.update {
                it.copy(
                    result = result.copy(date = getPersianDate(result.date)),
                    printStatus = printStatus
                )
            }
        }
    }
}
data class BalanceSuccessResultUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val printStatus: PrintStatus = PrintStatus.NO_PRINTING,
    val playbackSound: Boolean = false
)