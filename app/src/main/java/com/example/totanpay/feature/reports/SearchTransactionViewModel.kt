package com.example.totanpay.feature.reports

import android.util.Log
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
class SearchTransactionViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseSuccessResultUiState())
    val uiState: StateFlow<PurchaseSuccessResultUiState> = _uiState

    fun search(stan:String) {
        viewModelScope.launch {
            Log.d("TAG", "search() called->$stan")
            val lastTransaction = mainRepository.geTransactionBasedStan(stan)
            _uiState.update { it.copy(result = lastTransaction) }
        }
    }

}

