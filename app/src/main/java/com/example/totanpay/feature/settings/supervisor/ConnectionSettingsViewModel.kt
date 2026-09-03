package com.example.totanpay.feature.settings.supervisor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.log.LogRepository
import com.example.totanpay.data.repository.log.LogType
import com.example.totanpay.data.repository.settings.connection.ConnectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionSettingsViewModel @Inject constructor(private val connectionRepository: ConnectionRepository,
    private val logRepository: LogRepository) :
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
            if(ip!=_uiState.value.ip){
                logRepository.addLog(LogType.CHANGE_IP)
            }
            if(port!=_uiState.value.port){
                logRepository.addLog(LogType.CHANGE_PORT)
            }
            if(nii!=_uiState.value.nii){
                logRepository.addLog(LogType.CHANGE_NII)
            }
            connectionRepository.confirmConnectionSettings(ip, port, nii)
        }
    }

}

data class ConnectionSettingsUiState(
    val ip: String = "",
    val port: String = "",
    val nii: String = ""
)
