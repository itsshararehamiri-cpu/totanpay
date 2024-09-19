package com.example.totanpay.feature.bill

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.transaction.response.BaseTransactionResponse
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BillInquiryResultViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(BillInquiryResultTransactionUiState())
    val uiState: StateFlow<BillInquiryResultTransactionUiState> = _uiState

    fun init(response: String, billId: String, paymentId: String) {
        viewModelScope.launch {
            val result=Gson().fromJson(response,
                BaseTransactionResponse.BillInquiryTransactionResponse::class.java)
            _uiState.update { it.copy(result = result,billId=billId, paymentId = paymentId) }
            mainRepository.settlementReverse()
        }
    }

    fun print(bitmap: Bitmap, context: Context, onSuccess: () -> Unit, onFailed:(String)->Unit) {
        viewModelScope.launch {
            mainRepository.print(bitmap,context, onSuccess = {onSuccess()}, onFailed = {onFailed(it)})
        }
    }
}

data class BillInquiryResultTransactionUiState(
    val result: BaseTransactionResponse.BillInquiryTransactionResponse?=null,
    val error:String="",
    val billId:String="",
    val paymentId:String=""
)
