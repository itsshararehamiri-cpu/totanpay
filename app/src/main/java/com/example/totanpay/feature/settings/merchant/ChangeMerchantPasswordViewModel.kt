package com.example.totanpay.feature.settings.merchant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.merchant.MerchantSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChangeMerchantPasswordViewModel @Inject constructor(
    private val repository: MerchantSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ChangeMerchantPasswordUiState())
    val uiState: StateFlow<ChangeMerchantPasswordUiState> = _uiState


    fun setMerchantPassword(pass: String) {
        viewModelScope.launch {
            repository.setMerchantPassword(pass)
            _uiState.update { it.copy(confirmSettings=true) }
        }
    }


}

data class ChangeMerchantPasswordUiState(
    val confirmSettings: Boolean = false,

)

