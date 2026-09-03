package com.example.totanpay.feature.balance

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.totanpay.R
import com.example.totanpay.common.ui.ReadCardUiState
import com.example.totanpay.data.Operator
import com.example.totanpay.data.repository.CardReadResult
import com.example.totanpay.data.repository.DeviceRepository
import com.example.totanpay.data.repository.MainRepository
import com.example.totanpay.data.repository.datasource.formatAmount
import com.example.totanpay.data.repository.datasource.transaction.TransactionType
import com.example.totanpay.data.repository.settings.device_settings.DeviceSettingsRepository
import com.example.totanpay.data.repository.topup.TopUpRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadCardViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val deviceSettingsRepository: DeviceSettingsRepository,
    private val deviceRepository: DeviceRepository, private val topUpRepository: TopUpRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ReadCardUiState())
    val uiState: StateFlow<ReadCardUiState> = _uiState

    private val _isReading = mutableStateOf(false)
    private var isCardRead = false
    init {
        viewModelScope.launch {
            if (!mainRepository.isNetworkAvailable()) {
                _uiState.update {
                    it.copy(showInternetErrorMessage = true)
                }
            }
        }
    }

    fun readCard(context: Context) {
        viewModelScope.launch {

            val merchant = mainRepository.getMerchant()
            _uiState.update {
                it.copy(
                    merchantName = merchant?.merchantName ?: ""
                    , englishMerchantName = merchant?.englishMerchantName?:"",
                    merchantPhone = merchant?.merchantPhone ?: "",
                    playbackSound = deviceSettingsRepository.getPlaybackStatusSound(),
                    isTimeOut = false
                )
            }
            if (_isReading.value || isCardRead) {

            } else {
                _isReading.value = true
                when (val result = deviceRepository.readCard(context)) {
                    is CardReadResult.Success -> {
                        _uiState.update { it.copy(track2 = result.track2) }
                    }

                    is CardReadResult.Error -> {
                        _uiState.update { it.copy(error = result.message) }
                        retry("${context.getString(R.string.error)} ${result.message}",context)
                    }

                    CardReadResult.TimeOut -> {
                        _uiState.update { it.copy(isTimeOut = true) }
                    }
                }
            }
        }
    }

    private fun retry(message: String,context: Context) {
        Log.e("CardReader", message)
        _isReading.value = false
        viewModelScope.launch {
            delay(500)
            readCard(context = context)
        }
    }

    fun hideInternetErrorMessage() {
        viewModelScope.launch {
            _uiState.update { it.copy(showInternetErrorMessage = false) }
        }
    }

    fun getExtraMessage(
        type: TransactionType,
        amount: String,
        operator: String?,
        context: Context
    ) {
        viewModelScope.launch {
            if (type == TransactionType.TOPUP) {
                val operatorTemp = Gson().fromJson(operator, Operator::class.java)
                if (operatorTemp.code == 11) {
                    _uiState.update {
                        it.copy(
                            extraMessageValue = context.getString(
                                R.string.final_price_single_value,
                                topUpRepository.getTaxForCharge(),
                                topUpRepository.getAmountWithTax(amount, operatorTemp)
                                    .formatAmount()
                            )
                        )
                    }
                } else {
                    _uiState.update { it.copy(extraMessageValue = null) }
                }

            } else {
                _uiState.update { it.copy(extraMessageValue = null) }
            }
        }
    }
}
