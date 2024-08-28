package com.example.totanpay.feature.balance



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.ResultTransactionUiState
import com.example.totanpay.data.repository.MainRepository
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
class BalanceUnSuccessResultViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ResultTransactionUiState())
    val uiState: StateFlow<ResultTransactionUiState> = _uiState

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(response: String) {
        viewModelScope.launch {
            val result=Gson().fromJson(response,ResponseTransaction::class.java)
            _uiState.update { it.copy(result = result.copy(date = getPersianDate(result.date))) }
        }
    }

}

