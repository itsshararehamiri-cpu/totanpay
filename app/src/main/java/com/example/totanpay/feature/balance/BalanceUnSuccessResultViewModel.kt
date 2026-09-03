package com.example.totanpay.feature.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.common.PrintableViewModel
import com.example.totanpay.common.ResultTransactionUiState
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.util.getPersianDate
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BalanceUnSuccessResultViewModel @Inject constructor(private val deviceSettingsRepository: DeviceSettingsRepository,
    override val deviceRepository: DeviceRepository) :
    ViewModel(),PrintableViewModel {
    private val _uiState = MutableStateFlow(ResultTransactionUiState())
    val uiState: StateFlow<ResultTransactionUiState> = _uiState
    init {
        _uiState.update { it.copy(playbackSound = deviceSettingsRepository.getPlaybackStatusSound()) }
    }
    fun init(response: String) {
        viewModelScope.launch {
            val result=Gson().fromJson(response,ResponseTransaction::class.java)
            _uiState.update { it.copy(result = result.copy(date = getPersianDate(result.date))) }
        }
    }
    fun clearErrorMessage(){
        viewModelScope.launch {
            _uiState.update { it.copy(errorInPrint = "") }
        }
    }

    fun setErrorInPrint(errorInPrint: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(errorInPrint =errorInPrint) }
        }
    }
}

