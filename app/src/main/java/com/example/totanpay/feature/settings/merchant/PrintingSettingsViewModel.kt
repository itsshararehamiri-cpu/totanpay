package com.example.totanpay.feature.settings.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.PrintCustomerSettingsRepository
import com.example.totanpay.data.repository.PrintStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PrintingSettingsViewModel @Inject constructor(
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PrintingSettingsUiState())
    val uiState: StateFlow<PrintingSettingsUiState> = _uiState

    init {
        _uiState.update {
            it.copy(
                printStatus = printCustomerSettingsRepository.getPrintStatus(),
                minimumAmountForPrint =
                printCustomerSettingsRepository.getMinimumAmountForPrint()
            )
        }
    }

    fun setNoPrinting() {
        viewModelScope.launch {
            printCustomerSettingsRepository.setPrintStatus(PrintStatus.NO_PRINTING)
            _uiState.update { it.copy(confirmSettings = true) }
        }
    }

    fun setAlwaysPrinting() {
        viewModelScope.launch {
            printCustomerSettingsRepository.setPrintStatus(PrintStatus.ALWAYS_PRINTING)
            _uiState.update { it.copy(confirmSettings = true) }
        }
    }

    fun setPrintingWithMinAmount(minAmount: String) {
        viewModelScope.launch {
            printCustomerSettingsRepository.setPrintStatus(
                PrintStatus.PRINTING_WITH_MIN_AMOUNT,
                amount = minAmount
            )
            _uiState.update { it.copy(confirmSettings = true) }
        }
    }


}

data class PrintingSettingsUiState(
    val confirmSettings: Boolean = false,
    val printStatus: PrintStatus = PrintStatus.ALWAYS_PRINTING,
    val minimumAmountForPrint: String = ""

)

