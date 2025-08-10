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
class SearchTransactionViewModel @Inject constructor(private val reportRepository: ReportRepository,
                                                     override val deviceRepository: DeviceRepository
) :
    ViewModel(),PrintableViewModel {
    private val _uiState = MutableStateFlow(SearchTransactionUiState())
    val uiState: StateFlow<SearchTransactionUiState> = _uiState

    fun search(stan: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(showProgress = true,isInitState=false) }
            val searchedTransaction = reportRepository.geTransactionBasedStan(stan)
            if (searchedTransaction != null)
                _uiState.update { it.copy(result = searchedTransaction, showProgress = false, showNotFounding = false) }
            else {
                _uiState.update { it.copy(showNotFounding =true,showProgress=false) }
            }
        }
    }


}
data class SearchTransactionUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val showNotFounding: Boolean = false,
    val showProgress: Boolean = false,val isInitState:Boolean=true
)

