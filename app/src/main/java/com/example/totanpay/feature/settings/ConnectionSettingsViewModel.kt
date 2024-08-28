package com.example.totanpay.feature.settings

import androidx.lifecycle.ViewModel
import com.example.totanpay.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.totanpay.feature.balance.ReadCardUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jpos.iso.ISOUtil

@HiltViewModel
class ConnectionSettingsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionSettingsUiState())
    val uiState: StateFlow<ConnectionSettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ip = mainRepository.getIP(),
                    port = mainRepository.getPort(),
                    nii = mainRepository.getNii()
                )
            }
        }
    }

    fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        viewModelScope.launch {
            mainRepository.confirmConnectionSettings(ip, port, nii)
        }
    }

}

data class ConnectionSettingsUiState(
    val ip: String = "",
    val port: String = "",
    val nii: String = ""
)
