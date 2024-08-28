package com.example.totanpay.feature.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.google.gson.Gson
import com.urovo.i9000s.api.emv.ContantPara.TransactionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LastTransactionViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseSuccessResultUiState())
    val uiState: StateFlow<PurchaseSuccessResultUiState> = _uiState

    init {
        viewModelScope.launch {
            val lastTransaction = mainRepository.getLastTransaction()
            _uiState.update { it.copy(result = lastTransaction) }
        }
    }

}

data class PurchaseSuccessResultUiState(
    val result: ResponseTransaction? = null,
    val error: String = ""
)