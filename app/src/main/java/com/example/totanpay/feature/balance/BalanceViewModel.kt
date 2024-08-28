package com.example.totanpay.feature.balance

import androidx.lifecycle.ViewModel
import com.example.totanpay.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.datasource.ResponseData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
@HiltViewModel
class BalanceViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel (){
    private val _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState

    fun balance( track2: String,  pinBlock: String) {
        viewModelScope.launch {
            val result = mainRepository.balance(  track2, pinBlock)
            println("gggggggggghhhhhhhhh->${Gson().toJson(result)}")
            if(result is ResponseData.Success){
                println("ssssssssssusu")
                _uiState.update { it.copy(response = Gson().toJson(result.data), isSuccessful = true, isUnSuccessful = false) }
            }
            else{
                println("ssssssssssusun")
                _uiState.update { it.copy(response = Gson().toJson(result.data), isUnSuccessful = true, isSuccessful = false) }
                //    _uiState.update { it.copy(successResponse = Gson().toJson(result.error)) }
            }
        }
    }
}
data class BalanceUiState(
    val response: String="",
    val isSuccessful:Boolean=false,
    val isUnSuccessful:Boolean=false,

    )