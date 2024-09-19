package com.example.totanpay.feature.settings.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.ConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionSettingsViewModel @Inject constructor(private val connectionRepository: ConnectionRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(ConnectionSettingsUiState())
    val uiState: StateFlow<ConnectionSettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    ip = connectionRepository.getIP(),
                    port = connectionRepository.getPort(),
                    nii = connectionRepository.getNii()
                )
            }
        }
    }

    fun confirmConnectionSettings(ip: String, port: String, nii: String) {
        viewModelScope.launch {
            connectionRepository.confirmConnectionSettings(ip, port, nii)
        }
    }

}

data class ConnectionSettingsUiState(
    val ip: String = "",
    val port: String = "",
    val nii: String = ""
)
