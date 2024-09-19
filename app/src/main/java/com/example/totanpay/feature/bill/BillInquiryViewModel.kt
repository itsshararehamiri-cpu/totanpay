package com.example.totanpay.feature.bill


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.BillRepository
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
class BillInquiryViewModel @Inject constructor(private val billRepository: BillRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun inquiry(billId: String, paymentId: String) {
        viewModelScope.launch {
            val result = billRepository.billInquiry(billId, paymentId)
            if (result is ResponseData.Success) {

                _uiState.update {
                    it.copy(
                        response = Gson().toJson(result.data),
                        isSuccessful = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        response = Gson().toJson(result.data),
                        isUnSuccessful = true
                    )
                }
            }
        }
    }
}
