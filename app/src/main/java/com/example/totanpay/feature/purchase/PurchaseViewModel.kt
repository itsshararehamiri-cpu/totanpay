package com.example.totanpay.feature.purchase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.PurchaseRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PurchaseViewModel @Inject constructor(private val purchaseRepository: PurchaseRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState

    fun purchase( track2: String, amount: String, pinBlock: String,purchaseId:String?) {
        viewModelScope.launch {
            val result = purchaseRepository.purchase(amount, track2, pinBlock,purchaseId)
            if(result is ResponseData.Success){
                _uiState.update { it.copy(response = Gson().toJson(result.data), isSuccessful = true) }
            }
            else{
                _uiState.update { it.copy(response = Gson().toJson(result.data).toString(), isUnSuccessful = true) }
            }
        }
    }
}
data class PurchaseUiState(
    val response: String="",
    val isSuccessful:Boolean=false,
    val isUnSuccessful:Boolean=false)