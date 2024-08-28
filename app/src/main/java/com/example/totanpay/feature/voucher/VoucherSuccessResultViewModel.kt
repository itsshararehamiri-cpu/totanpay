package com.example.totanpay.feature.voucher

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class VoucherSuccessResultViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseSuccessResultUiState())
    val uiState: StateFlow<PurchaseSuccessResultUiState> = _uiState

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(response: String) {
        viewModelScope.launch {
            val result=Gson().fromJson(response,ResponseTransaction::class.java)
            _uiState.update { it.copy(result = result.copy(date = getPersianDate(result.date))) }        }
    }

}
data class PurchaseSuccessResultUiState(
    val result: ResponseTransaction?=null,
    val error:String=""
)