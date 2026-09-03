package com.example.totanpay.feature.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.voucher.VoucherRepository
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
class VoucherViewModel @Inject constructor(private val voucherRepository: VoucherRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun voucher(operator: String, track2: String, amount: String, pinBlock: String) {
        viewModelScope.launch {
            val result = voucherRepository.voucher(
                amount = amount, pan = "" ,
                track2 = track2, pinBlock = pinBlock, operator = Gson().fromJson(operator, Operator::class.java)
            )
            if (result is ResponseData.Success) {
                _uiState.update {
                    it.copy(
                        response = Gson().toJson(result.data),
                        isSuccessful = true
                    )
                }
            } else {
                if(result.data!=null)
                {
                    _uiState.update {
                        it.copy(
                            response = Gson().toJson(result.data),
                            isUnSuccessful = true
                        )
                    }
                }
                else{
                    _uiState.update { it.copy(connectionError = true) }
                }
            }
        }
    }
}
