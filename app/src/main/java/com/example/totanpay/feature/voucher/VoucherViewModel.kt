package com.example.totanpay.feature.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.feature.purchase.PurchaseUiState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VoucherViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun voucher(operator: String, track2: String, amount: String, pinBlock: String) {
        viewModelScope.launch {
            val temp: Operator =Gson().fromJson(operator, Operator::class.java)
            val result = mainRepository.voucher(amount, temp.code.toString(), track2, pinBlock)
            if(result is ResponseData.Success){
                _uiState.update { it.copy(response = Gson().toJson(result.data), isSuccessful = true) }
            }
            else{
                _uiState.update { it.copy(response = Gson().toJson(result.data), isUnSuccessful = true) }
            }
        }
    }
}
