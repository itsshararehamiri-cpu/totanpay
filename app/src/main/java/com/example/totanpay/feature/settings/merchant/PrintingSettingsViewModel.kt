package com.example.totanpay.feature.settings.merchant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import com.example.totanpay.data.repository.settings.merchant.PrintStatus
import com.example.totanpay.data.repository.settings.print_customer_setting.PrintCustomerSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrintingSettingsViewModel @Inject constructor(
    private val printCustomerSettingsRepository: PrintCustomerSettingsRepository,
    private val printMerchantSettingsRepository: MerchantSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PrintingSettingsUiState())
    val uiState: StateFlow<PrintingSettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    printStatusForCustomer = printCustomerSettingsRepository.getPrintStatus(),
                    minimumAmountForPrint =
                        printCustomerSettingsRepository.getMinimumAmountForPrint(),
                    printStatusForMerchant = printMerchantSettingsRepository.getPrinStatus(),
                    minimumAmountForMerchantPrint =
                        printMerchantSettingsRepository.getMinimumAmountForPrint(),
                    isAutoPrintCustomerReceipt = printCustomerSettingsRepository.getAutoPrintCustomerReceipt()
                )
            }
        }
    }

    fun saveReceiptSettings(
        customerStatus: PrintStatus,
        customerMinAmount: String?,
        autoPrintCustomerReceipt: Boolean,
        merchantStatus: PrintStatus) {
        viewModelScope.launch {
            printCustomerSettingsRepository.setPrintStatus(customerStatus, customerMinAmount,autoPrintCustomerReceipt)
            printMerchantSettingsRepository.setPrintStatus(merchantStatus)
            _uiState.update { it.copy(confirmSettings = true) }
        }
    }
}

data class PrintingSettingsUiState(
    val confirmSettings: Boolean = false,
    val printStatusForCustomer: PrintStatus = PrintStatus.NO_PRINTING,
    val printStatusForMerchant: PrintStatus = PrintStatus.NO_PRINTING,
    val isAutoPrintCustomerReceipt: Boolean=false,
    val minimumAmountForPrint: String = "",
    val minimumAmountForMerchantPrint: String = ""
)

