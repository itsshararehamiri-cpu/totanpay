package com.example.totanpay.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.PrintableViewModel
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.settings.report.ReportRepository
import com.example.totanpay.ui.getPersianDateFrom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject


@HiltViewModel
class DetailsTransactionViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val mainRepository: MainRepository,
    override val deviceRepository: DeviceRepository) : ViewModel(), PrintableViewModel {

    private val _uiState = MutableStateFlow(DetailsTransactionUiState())
    val uiState: StateFlow<DetailsTransactionUiState> = _uiState
    fun search(
        fromDate: PersianDate?,
        toDate: PersianDate?,
        fromAmount: String?,
        toAmount: String?,
        selectedTransactions: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true) }
            val transactions = reportRepository.getDetailsTransaction(
                fromDate, toDate, fromAmount, toAmount, selectedTransactions
            )
            if (!transactions.isNullOrEmpty()) {
                val merchant = mainRepository.getMerchant()
                _uiState.update {
                    it.copy(
                        result = transactions,
                        showProgress = false,
                        fromDate = getPersianDateFrom(
                            year = fromDate!!.shYear,
                            month = fromDate.shMonth,
                            day = fromDate.shDay
                        ),
                        toDate = getPersianDateFrom(
                            year = toDate!!.shYear,
                            month = toDate.shMonth,
                            day = toDate.shDay
                        ),
                        fromTime = "${fromDate.hour}:${fromDate.minute}",
                        toTime = "${toDate.hour}:${toDate.minute}",
                        fromAmount = fromAmount ?: "",
                        toAmount = toAmount ?: "",
                        selectedTransactionTypes = selectedTransactions ?: "",
                        numberOfTransactions = transactions.size.toString(),
                        sumOfTransactions = transactions.sumOf {transaction->
                            transaction.amount.toLong()
                        }.toString(),
                        merchantPhone = merchant?.merchantPhone.toString(),
                        merchantName =merchant?.merchantName.toString(),
                        englishMerchantName=  merchant?.englishMerchantName.toString(),
                        merchantId = merchant?.merchantId ?: "",
                        terminalId = mainRepository.getTerminalId() ?: ""
                    )
                }
            } else {
                _uiState.update { it.copy(showNotFound = true, showProgress = false) }
            }
        }
    }

}

data class DetailsTransactionUiState(
    val showProgress: Boolean = false,
    val result: List<ResponseTransaction>? = null,
    val dates: List<String> = emptyList(),
    val error: String = "",
    val showNotFound: Boolean = false,
    val sumOfTransactions: String = "",
    val numberOfTransactions: String = "",
    val terminalId: String = "",
    val merchantId: String = "",
    val merchantPhone: String = "",
    val merchantName: String = "",
    val englishMerchantName: String = "",

    val fromDate: String = "",
    val fromTime: String = "",
    val toDate: String = "",
    val toTime: String = "",
    val fromAmount: String = "",
    val toAmount: String = "",
    val selectedTransactionTypes: String = ""
)