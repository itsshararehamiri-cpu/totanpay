package com.example.totanpay.feature.purchase

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.getPersianDate
import com.example.totanpay.util.convertPurchaseResultToJsonObject
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PurchaseUnSuccessResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val deviceSettingsRepository: DeviceSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PurchaseUnSuccessUiState())
    val uiState: StateFlow<PurchaseUnSuccessUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    playbackSound = deviceSettingsRepository.getPlaybackStatusSound()
                )
            }
        }
    }

    fun init(response: String) {
        viewModelScope.launch {
            val responseTransaction = Gson().fromJson(response, ResponseTransaction::class.java)
            _uiState.update {
                it.copy(
                    autoPrint = (responseTransaction.responseCode == "-1"),
                    result = responseTransaction.copy(
                        date = getPersianDate(
                            responseTransaction.date
                        )
                    )
                )
            }
        }
    }
    fun clearErrorMessage(){
        viewModelScope.launch {
            _uiState.update { it.copy(errorInPrint = "") }
        }
    }
    fun getResponseFroCallerApp(response: String) {
        viewModelScope.launch {
            val result = convertPurchaseResultToJsonObject(response)
            _uiState.update { it.copy(responseForCallerApp = result) }
        }
    }

    fun printAndConfirm(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            mainRepository.print(bitmap, context, onSuccess = {}, onFailed = {errorMessage->
                _uiState.update { it.copy(errorInPrint=errorMessage) }
            })
        }
    }
}

data class PurchaseUnSuccessUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val autoPrint: Boolean = false,
    val responseForCallerApp: String? = null,
    val playbackSound: Boolean = false,val errorInPrint: String=""
)
