package com.example.totanpay.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.ResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupervisorSettingsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SupervisorSettingsUiState())
    val uiState: StateFlow<SupervisorSettingsUiState> = _uiState

    //    init {
//        viewModelScope.launch {
//            mainRepository.logon()
//            mainRepository.init()
//        }
//    }
    fun keyInjection() {
        viewModelScope.launch {
            mainRepository.keyInjection()
          //  mainRepository.getKeyTransaction("123456")
        }
    }

    fun configuration() {
        viewModelScope.launch {
            val logonResult = mainRepository.logon()
            if (logonResult is ResponseData.Success) {
                val initResult = mainRepository.init()
                if (initResult is ResponseData.Success)
                {
                    _uiState.update { it.copy(success = true) }
                    delay(5000)
                    _uiState.update { it.copy(success = false) }
                }
                else {
                    _uiState.update { it.copy(error = initResult.data?.responseMessage?:"") }
                }
            } else {
                if (!logonResult.error.isNullOrEmpty()) {
                    _uiState.update { it.copy(error = logonResult.error) }
                } else {
                    _uiState.update { it.copy(error = logonResult.data?.responseMessage?:"") }

                }
            }
        }
    }
}

data class SupervisorSettingsUiState(
    val success: Boolean = false,
    val error: String = ""
)
