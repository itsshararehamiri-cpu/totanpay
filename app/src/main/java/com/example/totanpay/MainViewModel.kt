package com.example.totanpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        println("init GetPingggViewModel")
    }

    fun isConfigured() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }// TODO: xata ertenat

    fun purchase() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            if (isConfigured) {
                _uiState.update { it.copy(purchase = true) }
            } else _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }
    fun balance() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            if (isConfigured) {
                _uiState.update { it.copy(balance = true) }
            } else _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }
    fun voucher() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            if (isConfigured) {
                _uiState.update { it.copy(voucher = true) }
            } else _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }
    fun topup() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            if (isConfigured) {
                _uiState.update { it.copy(topup = true) }
            } else _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }
    fun billPay() {
        viewModelScope.launch {
            val isConfigured = mainRepository.isConfigured()
            if (isConfigured) {
                _uiState.update { it.copy(billPay = true) }
            } else _uiState.update { it.copy(isConfigured = isConfigured) }
        }
    }
}

data class MainUiState(
    val isConfigured: Boolean = true,
    val haveErrorConnection:Boolean=false,
    val purchase: Boolean = false,
    val balance: Boolean = false,
    val billPay: Boolean = false,
    val voucher: Boolean = false,
    val topup: Boolean = false


)
