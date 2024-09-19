package com.example.totanpay.feature.topup


import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.data.repository.DeviceSettingsRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.model.ResponseTransaction
import com.example.totanpay.data.util.getPersianDate
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopUpUnSuccessResultViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val deviceSettingsRepository: DeviceSettingsRepository
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(TopUpUnSuccessUiState())
    val uiState: StateFlow<TopUpUnSuccessUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(playbackSound = deviceSettingsRepository.getPlaybackStatusSound()) }
        }
    }

    fun init(response: String) {
        viewModelScope.launch {
            val result = Gson().fromJson(response, ResponseTransaction::class.java)
            _uiState.update { it.copy(result = result.copy(date = getPersianDate(result.date))) }
        }
    }

    fun printAndConfirm(bitmap: Bitmap, context: Context) {
        viewModelScope.launch {
            delay(3000)
            mainRepository.print(bitmap, context, onSuccess = {}, onFailed = {})
            mainRepository.settlementReverse()
        }
    }

}

data class TopUpUnSuccessUiState(
    val result: ResponseTransaction? = null,
    val error: String = "",
    val autoPrint: Boolean = true,
    val playbackSound: Boolean = false
)
