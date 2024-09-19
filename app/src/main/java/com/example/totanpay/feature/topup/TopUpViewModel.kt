package com.example.totanpay.feature.topup



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.TopUpRepository
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
class TopUpViewModel @Inject constructor(private val topUpRepository: TopUpRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun topup( track2: String, amount: String,mobile:String,operator: String, pinBlock: String) {

        viewModelScope.launch {
            val result = topUpRepository.topUp(amount=amount, mobile=mobile, operator = Gson().fromJson(operator, Operator::class.java), track2 =  track2,pinBlock= pinBlock)
            if(result is ResponseData.Success){
                _uiState.update { it.copy(response = Gson().toJson(result.data), isSuccessful = true) }
            }
            else{
                _uiState.update { it.copy(response = Gson().toJson(result.data), isUnSuccessful = true) }
            }
        }
    }
}
