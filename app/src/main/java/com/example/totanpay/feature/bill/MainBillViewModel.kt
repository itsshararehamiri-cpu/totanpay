package com.example.totanpay.feature.bill

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainBillViewModel @Inject constructor(
                                            private val deviceRepository: DeviceRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(MainBillUiState())
    val uiState: StateFlow<MainBillUiState> = _uiState
    fun scan(context: Context) {
        viewModelScope.launch {
            deviceRepository.scan(context,onSuccess = {barCode->
                _uiState.update { it.copy(barcode =barCode ) }
            }, onError = {errorMessage->
                _uiState.update { it.copy(errorInReadBarCode =errorMessage ) }
            }, onTimeout = {
                _uiState.update { it.copy( isTimeOut=true) }
            }, onCancel = {
                _uiState.update { it.copy( isCancel=true) }
            })
        }
    }
}

data class MainBillUiState(
    val barcode: String = "",
    val errorInReadBarCode: String = "",
    val isCancel: Boolean = false,
    val isTimeOut: Boolean = false)
