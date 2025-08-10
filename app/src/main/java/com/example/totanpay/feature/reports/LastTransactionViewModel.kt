package com.example.totanpay.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.PrintableViewModel
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.ReportRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LastTransactionViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    override val deviceRepository: DeviceRepository
) : ViewModel(), PrintableViewModel {
    private val _uiState = MutableStateFlow(PurchaseSuccessResultUiState())
    val uiState: StateFlow<PurchaseSuccessResultUiState> = _uiState

    init {
        viewModelScope.launch {
            val lastTransaction = reportRepository.getLastTransaction()
            if (lastTransaction != null) {
                _uiState.update {
                    it.copy(
                        result = lastTransaction, showProgress = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        showNotFounding = true, showProgress = false
                    )
                }
            }
        }
    }


}

data class PurchaseSuccessResultUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val showNotFounding: Boolean = false,
    val showProgress: Boolean = true

)