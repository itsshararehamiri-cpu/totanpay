package com.example.totanpay.feature.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.balance.BalanceRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import com.example.totanpay.data.repository.datasource.TotanPayException
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(private val balanceRepository: BalanceRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState
    fun balance(track2: String, pinBlock: String) {
        viewModelScope.launch {
            val result = balanceRepository.balance(track2, pinBlock)
            if (result is ResponseData.Success) {
                _uiState.update {
                    it.copy(
                        response = Gson().toJson(result.data),
                        isSuccessful = true,
                        isUnSuccessful = false
                    )
                }
            } else {
                if(result.data!=null)
                {
                    _uiState.update {
                        it.copy(
                            response = Gson().toJson(result.data),
                            isUnSuccessful = true,
                            isSuccessful = false
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

data class BalanceUiState(
    val response: String = "",
    val isSuccessful: Boolean = false,
    val isUnSuccessful: Boolean = false,
    val connectionError: Boolean=false

    )